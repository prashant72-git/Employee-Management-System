package com.example.ems.employee;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeRepository employeeRepository;

    //  Get all employees
    @GetMapping
    public List<Employee> list() {
        return employeeRepository.findAll();
    }

    //  Create a new employee
    @PostMapping
    public ResponseEntity<Employee> create(@Valid @RequestBody Employee e) {
        Employee saved = employeeRepository.save(e);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    //  Get single employee by ID
    @GetMapping("/{id}")
    public ResponseEntity<Employee> get(@PathVariable("id") Long id) {
        return employeeRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update employee by ID
    @PutMapping("/{id}")
    public ResponseEntity<Employee> update(@PathVariable("id") Long id,
                                           @Valid @RequestBody Employee e) {
        return employeeRepository.findById(id).map(existing -> {
            e.setId(existing.getId()); // keep the same ID
            Employee updated = employeeRepository.save(e);
            return ResponseEntity.ok(updated);
        }).orElse(ResponseEntity.notFound().build());
    }

    //  Delete employee
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        if (employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    //  Simple exception handler for clarity
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: " + ex.getMessage());
    }
}
