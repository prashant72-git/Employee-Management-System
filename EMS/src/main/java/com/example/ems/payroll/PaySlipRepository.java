package com.example.ems.payroll;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaySlipRepository extends JpaRepository<PaySlip, Long> {
    List<PaySlip> findByPayRunId(Long runId);
}
