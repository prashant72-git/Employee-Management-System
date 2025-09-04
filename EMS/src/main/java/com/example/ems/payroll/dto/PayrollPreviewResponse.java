package com.example.ems.payroll.dto;

import java.util.List;

public record PayrollPreviewResponse(
        Long runId,
        String month,
        String status,
        List<PayrollPreviewItem> items
) {}
