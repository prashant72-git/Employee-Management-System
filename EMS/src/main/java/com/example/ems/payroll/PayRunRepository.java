package com.example.ems.payroll;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PayRunRepository extends JpaRepository<PayRun, Long> {
    Optional<PayRun> findByRunYearAndRunMonth(Integer y, Integer m);
}
