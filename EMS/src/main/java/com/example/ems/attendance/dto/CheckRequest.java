package com.example.ems.attendance.dto;

import jakarta.validation.constraints.NotNull;

public record CheckRequest(
        @NotNull Long employeeId,
        @NotNull String timestampUtc // ISO string
) {}
