package com.example.ems.attendance;

import com.example.ems.employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<AttendanceRecord, Long> {
    Optional<AttendanceRecord> findByEmployeeAndWorkDate(Employee employee, LocalDate workDate);
    long countByEmployeeIdAndWorkDateBetween(Long employeeId, LocalDate start, LocalDate end);
}
