package com.example.ems.attendance;

import com.example.ems.attendance.dto.CheckRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping("/check-in")
    public AttendanceRecord checkIn(@Valid @RequestBody CheckRequest req) {
        OffsetDateTime ts = parse(req.timestampUtc());
        return attendanceService.checkIn(req.employeeId(), ts);
    }

    @PostMapping("/check-out")
    public AttendanceRecord checkOut(@Valid @RequestBody CheckRequest req) {
        OffsetDateTime ts = parse(req.timestampUtc());
        return attendanceService.checkOut(req.employeeId(), ts);
    }

    private OffsetDateTime parse(String iso) {
        try { return OffsetDateTime.parse(iso); }
        catch (DateTimeParseException e) { throw new IllegalArgumentException("Invalid ISO timestamp: " + iso); }
    }
}
