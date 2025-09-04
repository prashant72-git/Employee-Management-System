package com.example.ems.attendance;

import com.example.ems.common.Enums.AttendanceStatus;
import com.example.ems.employee.Employee;
import com.example.ems.employee.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;

    private static final int WORKDAY_MINUTES = 480;
    private static final int HALF_DAY_THRESHOLD = 240;
    private static final int GRACE_MINUTES = 10;

    @Transactional
    public AttendanceRecord checkIn(Long employeeId, OffsetDateTime ts) {
        Employee emp = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found: " + employeeId));

        LocalDate d = ts.toLocalDate();
        Optional<AttendanceRecord> existing = attendanceRepository.findByEmployeeAndWorkDate(emp, d);
        if (existing.isPresent()) {
            AttendanceRecord rec = existing.get();
            if (rec.getCheckIn() != null) return rec;
            rec.setCheckIn(ts);
            return attendanceRepository.save(rec);
        }

        AttendanceRecord rec = AttendanceRecord.builder()
                .employee(emp)
                .workDate(d)
                .checkIn(ts)
                .status(null)
                .build();
        return attendanceRepository.save(rec);
    }

    @Transactional
    public AttendanceRecord checkOut(Long employeeId, OffsetDateTime ts) {
        Employee emp = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found: " + employeeId));

        LocalDate d = ts.toLocalDate();
        AttendanceRecord rec = attendanceRepository.findByEmployeeAndWorkDate(emp, d)
                .orElseThrow(() -> new IllegalStateException("No check-in found for today"));

        rec.setCheckOut(ts);
        // compute minutes
        int minutes = (int) Duration.between(rec.getCheckIn(), rec.getCheckOut()).toMinutes();
        rec.setMinutesWorked(Math.max(minutes, 0));
        rec.setStatus(deriveStatus(rec.getCheckIn().toLocalTime(), minutes));
        return attendanceRepository.save(rec);
    }

    private AttendanceStatus deriveStatus(LocalTime checkInLocalTime, int minutesWorked) {
        LocalTime shiftStart = LocalTime.of(9, 0);
        boolean late = checkInLocalTime.isAfter(shiftStart.plusMinutes(GRACE_MINUTES));
        if (minutesWorked < HALF_DAY_THRESHOLD) return AttendanceStatus.HALF_DAY;
        if (late) return AttendanceStatus.LATE;
        if (minutesWorked >= WORKDAY_MINUTES) return AttendanceStatus.PRESENT;
        return AttendanceStatus.PRESENT;
    }

    @Transactional
    public void nightlyReconcile(LocalDate date) {
        // close any open records: if no checkout -> mark as ABSENT/HALF_DAY
        // (Simplified for MVP)
    }
}
