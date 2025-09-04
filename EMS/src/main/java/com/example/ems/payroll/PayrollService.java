package com.example.ems.payroll;

import com.example.ems.attendance.AttendanceRepository;
import com.example.ems.employee.Employee;
import com.example.ems.employee.EmployeeRepository;
import com.example.ems.payroll.dto.PayrollPreviewItem;
import com.example.ems.payroll.dto.PayrollPreviewResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import static com.example.ems.common.Enums.PayRunStatus.DRAFT;
import static com.example.ems.common.Enums.PayRunStatus.LOCKED;

@Service
@RequiredArgsConstructor
public class PayrollService {

    private final EmployeeRepository employeeRepository;
    private final AttendanceRepository attendanceRepository;
    private final PayRunRepository payRunRepository;
    private final PaySlipRepository paySlipRepository;
    private final PaySalaryTemplateRepository templateRepository;

    @Transactional
    public PayrollPreviewResponse previewRun(YearMonth ym) {
        PayRun run = payRunRepository.findByRunYearAndRunMonth(ym.getYear(), ym.getMonthValue())
                .orElseGet(() -> payRunRepository.save(PayRun.builder()
                        .runYear(ym.getYear())
                        .runMonth(ym.getMonthValue())
                        .status(DRAFT)
                        .createdBy("system")
                        .build()));

        List<Employee> emps = employeeRepository.findAll();
        int workingDays = ym.lengthOfMonth(); // simplified (no holidays/weekends)
        LocalDate start = ym.atDay(1);
        LocalDate end = ym.atEndOfMonth();

        List<PayrollPreviewItem> items = new ArrayList<>();
        for (Employee e : emps) {
            long present = attendanceRepository.countByEmployeeIdAndWorkDateBetween(e.getId(), start, end);
            int unpaidDays = Math.max(0, workingDays - (int) present);
            double basicPct = 0.6, hraPct = 0.2, allowanceFixed = 5000, pfPct = 0.12, taxPct = 0.05;
            double basic = e.getBaseSalary() * basicPct;
            double hra = e.getBaseSalary() * hraPct;
            double allowances = allowanceFixed;
            double overtimePay = 0;
            double perDayRate = e.getBaseSalary() / workingDays;
            double unpaidDeduction = perDayRate * unpaidDays;
            double pf = basic * pfPct;
            double gross = basic + hra + allowances + overtimePay;
            double tax = gross * taxPct;
            double net = gross - (pf + tax + unpaidDeduction);
            items.add(new PayrollPreviewItem(
                    e.getId(), e.getCode(), workingDays, (int) present, unpaidDays,
                    round(basic), round(hra), round(allowances), round(overtimePay),
                    round(pf), round(tax), round(unpaidDeduction), round(gross), round(net)
            ));
        }
        return new PayrollPreviewResponse(run.getId(), ym.toString(), run.getStatus().name(), items);
    }

    @Transactional
    public Long lockRun(YearMonth ym) {
        PayRun run = payRunRepository.findByRunYearAndRunMonth(ym.getYear(), ym.getMonthValue())
                .orElseThrow(() -> new IllegalStateException("Preview not created for " + ym));
        if (run.getStatus() == LOCKED) return run.getId();

        PayrollPreviewResponse preview = previewRun(ym);
        // create payslips
        preview.items().forEach(i -> {
            PaySlip ps = PaySlip.builder()
                    .payRun(run)
                    .employee(employeeRepository.findById(i.employeeId()).orElseThrow())
                    .basic(i.basic())
                    .hra(i.hra())
                    .allowances(i.allowances())
                    .overtimePay(i.overtimePay())
                    .pf(i.pf())
                    .tax(i.tax())
                    .unpaidDeduction(i.unpaidDeduction())
                    .gross(i.gross())
                    .net(i.net())
                    .remarks("Locked")
                    .build();
            paySlipRepository.save(ps);
        });
        run.setStatus(LOCKED);
        return run.getId();
    }

    private double round(double v) { return Math.round(v * 100.0) / 100.0; }
}
