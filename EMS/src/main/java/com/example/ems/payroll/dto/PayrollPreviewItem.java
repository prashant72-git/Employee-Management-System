package com.example.ems.payroll.dto;

public record PayrollPreviewItem(
        Long employeeId,
        String employeeCode,
        int workingDays,
        int presentDays,
        int unpaidDays,
        double basic,
        double hra,
        double allowances,
        double overtimePay,
        double pf,
        double tax,
        double unpaidDeduction,
        double gross,
        double net
) {}
