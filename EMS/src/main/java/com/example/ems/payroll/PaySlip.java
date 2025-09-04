package com.example.ems.payroll;

import com.example.ems.employee.Employee;
import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "pay_slips",
        uniqueConstraints = @UniqueConstraint(columnNames = {"pay_run_id","employee_id"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PaySlip {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "pay_run_id")
    private PayRun payRun;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "employee_id")
    private Employee employee;

    private Double basic;
    private Double hra;
    private Double allowances;
    private Double overtimePay;
    private Double pf;
    private Double tax;
    private Double unpaidDeduction;
    private Double gross;
    private Double net;
    private String remarks;
    private String pdfPath;
}
