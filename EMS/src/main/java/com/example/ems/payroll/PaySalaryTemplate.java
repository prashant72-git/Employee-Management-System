package com.example.ems.payroll;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "pay_salary_templates")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PaySalaryTemplate {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    private Double basicPct;      // e.g. 0.6
    private Double hraPct;        // e.g. 0.2
    private Double allowanceFixed;// e.g. 5000
    private Double pfPct;         // e.g. 0.12
    private Double taxPct;        // simplified
    private Double overtimeRatePerHour;
}
