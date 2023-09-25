package com.epam.rd.autotasks.springemployeecatalog.repository;

import com.epam.rd.autotasks.springemployeecatalog.domain.Employee;
import org.springframework.data.domain.Pageable;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface EmployeeRepository {
    List<Employee> findAllEmployee(Pageable pageable) throws SQLException;

    Optional<Employee> findByIdWIthManager(Long id);

    Optional<Employee> findByIdWithManagersManager(Long id);

    List<Employee> findEmployeesByManager(Long managerId, Pageable pageable);

    List<Employee> findEmployeesByDepId(Long id, Pageable pageable);

    List<Employee> findEmployeesByDepName(String value, Pageable pageable);
}