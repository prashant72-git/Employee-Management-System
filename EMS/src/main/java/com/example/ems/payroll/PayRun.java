package com.example.ems.payroll;

import com.example.ems.common.Enums.PayRunStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.YearMonth;

@Entity @Table(name = "pay_runs")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PayRun {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer runYear;
    private Integer runMonth; // 1-12

    @Enumerated(EnumType.STRING)
    private PayRunStatus status = PayRunStatus.DRAFT;

    private String createdBy;
    private Instant createdAt;
    private Instant lockedAt;

    public YearMonth asYearMonth() {
        return YearMonth.of(runYear, runMonth);
    }
}
