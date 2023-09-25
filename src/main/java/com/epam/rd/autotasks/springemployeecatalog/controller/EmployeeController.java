package com.epam.rd.autotasks.springemployeecatalog.controller;

import com.epam.rd.autotasks.springemployeecatalog.domain.Employee;
import com.epam.rd.autotasks.springemployeecatalog.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Employee> getEmployees(Pageable pageable) throws SQLException {
        return employeeService.getEmployees(pageable);
    }

    @GetMapping(value = "/{employeeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<Employee> getEmployee(@PathVariable String employeeId, @RequestParam(required = false) boolean full_chain) {
        return employeeService.getEmployeeById(employeeId, full_chain);
    }

    @GetMapping(value = "/by_manager/{managerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Employee> getEmployeesByManager(@PathVariable String managerId, Pageable pageable) {
        return employeeService.getEmployeesByManager(managerId, pageable);
    }

    @GetMapping(value = "/by_department/{value}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Employee> getEmployeesByDepIdOrDepName(@PathVariable String value, Pageable pageable) {
        return employeeService.getEmployeesByDepIdOrDepName(value, pageable);
    }
}