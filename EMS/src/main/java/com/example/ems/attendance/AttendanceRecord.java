package com.example.ems.attendance;

import com.example.ems.common.Enums.AttendanceSource;
import com.example.ems.common.Enums.AttendanceStatus;
import com.example.ems.employee.Employee;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity @Table(name = "attendance_records",
       uniqueConstraints = @UniqueConstraint(columnNames = {"employee_id","work_date"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AttendanceRecord {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(name = "work_date", nullable = false)
    private LocalDate workDate;

    private OffsetDateTime checkIn;
    private OffsetDateTime checkOut;

    private Integer minutesWorked;

    @Enumerated(EnumType.STRING)
    private AttendanceStatus status;

    @Enumerated(EnumType.STRING)
    private AttendanceSource source = AttendanceSource.WEB;
}
