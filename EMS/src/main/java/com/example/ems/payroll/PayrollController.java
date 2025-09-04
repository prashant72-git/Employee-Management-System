package com.example.ems.payroll;

import com.example.ems.payroll.dto.PayrollPreviewResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;

@RestController
@RequestMapping("/api/payroll")
@RequiredArgsConstructor
public class PayrollController {

    private final PayrollService payrollService;

    @PostMapping("/runs/preview")
    public PayrollPreviewResponse preview(@RequestParam("month") String month) {
        YearMonth ym = YearMonth.parse(month); // e.g., 2025-08
        return payrollService.previewRun(ym);
    }

    @PostMapping("/runs/{month}/lock")
    public Long lock(@PathVariable("month") String month) {
        YearMonth ym = YearMonth.parse(month);
        return payrollService.lockRun(ym);
    }

}
