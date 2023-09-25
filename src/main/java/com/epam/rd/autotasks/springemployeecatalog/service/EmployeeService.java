package com.epam.rd.autotasks.springemployeecatalog.service;

import com.epam.rd.autotasks.springemployeecatalog.constants.Constant;
import com.epam.rd.autotasks.springemployeecatalog.domain.Employee;
import com.epam.rd.autotasks.springemployeecatalog.repository.EmployeeRepository;
import com.epam.rd.autotasks.springemployeecatalog.repository.EmployeeRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepositoryImpl employeeDaoImpl) {
        this.employeeRepository = employeeDaoImpl;
    }

    public List<Employee> getEmployees(Pageable pageable) throws SQLException {
        return employeeRepository.findAllEmployee(pageable);
    }

    public Optional<Employee> getEmployeeById(String id, boolean fullChain) {
        if (fullChain) {
            return employeeRepository.findByIdWithManagersManager(Long.parseLong(id));
        } else {
            return employeeRepository.findByIdWIthManager(Long.parseLong(id));
        }
    }

    public List<Employee> getEmployeesByManager(String managerId, Pageable pageable) {
        return employeeRepository.findEmployeesByManager(Long.parseLong(managerId), pageable);
    }

    public List<Employee> getEmployeesByDepIdOrDepName(String value, Pageable pageable) {
        if (value.matches(Constant.REGEX_NUMBER)) {
            return employeeRepository.findEmployeesByDepId(Long.parseLong(value), pageable);
        } else {
            return employeeRepository.findEmployeesByDepName(value, pageable);
        }
    }
}