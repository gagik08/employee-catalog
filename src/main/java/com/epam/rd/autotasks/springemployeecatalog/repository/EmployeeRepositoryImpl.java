package com.epam.rd.autotasks.springemployeecatalog.repository;

import com.epam.rd.autotasks.springemployeecatalog.domain.Employee;
import com.epam.rd.autotasks.springemployeecatalog.extractor.EmployeeResultSetExtractorManagersManager;
import com.epam.rd.autotasks.springemployeecatalog.extractor.EmployeeResultSetExtractor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.epam.rd.autotasks.springemployeecatalog.constants.Constant.ID;
import static com.epam.rd.autotasks.springemployeecatalog.constants.SQLQuery.ALL_FROM_EMPLOYEE_FORMAT_QUERY;
import static com.epam.rd.autotasks.springemployeecatalog.constants.SQLQuery.EMPLOYEES_BY_DEP_ID_FORMAT_QUERY;
import static com.epam.rd.autotasks.springemployeecatalog.constants.SQLQuery.EMPLOYEES_BY_DEP_NAME_FORMAT_QUERY;
import static com.epam.rd.autotasks.springemployeecatalog.constants.SQLQuery.EMPLOYEES_BY_MANAGER_FORMAT_QUERY;
import static com.epam.rd.autotasks.springemployeecatalog.constants.SQLQuery.EMPLOYEE_BY_ID_FORMAT_QUERY;

@Repository
public class EmployeeRepositoryImpl implements EmployeeRepository {
    private final JdbcTemplate jdbcTemplate;
    private final EmployeeResultSetExtractor employeeResultSetExtractor;
    private final EmployeeResultSetExtractorManagersManager employeeResultSetExtractorManagersManager;

    @Autowired
    public EmployeeRepositoryImpl(JdbcTemplate jdbcTemplate,
                                  EmployeeResultSetExtractor employeeResultSetExtractor,
                                  EmployeeResultSetExtractorManagersManager employeeResultSetExtractorManagersManager) {
        this.jdbcTemplate = jdbcTemplate;
        this.employeeResultSetExtractor = employeeResultSetExtractor;
        this.employeeResultSetExtractorManagersManager = employeeResultSetExtractorManagersManager;
    }

    @Override
    public List<Employee> findAllEmployee(Pageable pageable) {
        Order order = getOrder(pageable);
        String formatQuery = String.format(ALL_FROM_EMPLOYEE_FORMAT_QUERY, order.getProperty(), order.getDirection().name());
        return getPageableEmployees(pageable, getEmployeesWithManager(formatQuery));
    }

    @Override
    public Optional<Employee> findByIdWIthManager(Long id) {
        String formatQuery = String.format(EMPLOYEE_BY_ID_FORMAT_QUERY, id);
        List<Employee> employees = getEmployeesWithManager(formatQuery);
        return Optional.ofNullable(Objects.requireNonNull(employees).get(0));
    }

    @Override
    public Optional<Employee> findByIdWithManagersManager(Long id) {
        String formatQuery = String.format(EMPLOYEE_BY_ID_FORMAT_QUERY, id);
        List<Employee> employees = jdbcTemplate.query(getPreparedStatementCreator(formatQuery), employeeResultSetExtractorManagersManager);
        return Optional.ofNullable(Objects.requireNonNull(employees).get(0));
    }

    @Override
    public List<Employee> findEmployeesByManager(Long managerId, Pageable pageable) {
        Order order = getOrder(pageable);
        String formatQuery = String.format(EMPLOYEES_BY_MANAGER_FORMAT_QUERY, managerId, order.getProperty(), order.getDirection().name());
        List<Employee> employees = getEmployeesWithManager(formatQuery);
        return getPageableEmployees(pageable, getEmployeesListFilteredByManagerId(managerId, employees));
    }

    @Override
    public List<Employee> findEmployeesByDepId(Long id, Pageable pageable) {
        Order order = getOrder(pageable);
        String formatQuery = String.format(EMPLOYEES_BY_DEP_ID_FORMAT_QUERY, id, order.getProperty(), order.getDirection().name());
        List<Employee> employees = getEmployeesWithManager(formatQuery);
        return getPageableEmployees(pageable, getEmployeesListFilteredByDepId(id, employees));
    }

    @Override
    public List<Employee> findEmployeesByDepName(String depName, Pageable pageable) {
        Order order = getOrder(pageable);
        String formatQuery = String.format(EMPLOYEES_BY_DEP_NAME_FORMAT_QUERY, depName, order.getProperty(), order.getDirection().name());
        List<Employee> employees = getEmployeesWithManager(formatQuery);
        return getPageableEmployees(pageable, getEmployeesFilteredByDepName(Objects.requireNonNull(employees), depName));
    }

    private List<Employee> getEmployeesListFilteredByManagerId(Long managerId, List<Employee> employees) {
        return Objects.requireNonNull(employees).stream()
                .filter(employee -> employee.getManager() != null && employee.getManager().getId().equals(managerId))
                .collect(Collectors.toList());
    }

    private List<Employee> getEmployeesListFilteredByDepId(Long id, List<Employee> employees) {
        return Objects.requireNonNull(employees).stream()
                .filter(employee -> employee.getManager() != null && employee.getDepartment().getId().equals(id))
                .collect(Collectors.toList());
    }

    private List<Employee> getEmployeesFilteredByDepName(List<Employee> employees, String depName) {
        return employees.stream()
                .filter(employee -> employee.getDepartment() != null && employee.getDepartment().getName().equals(depName))
                .collect(Collectors.toList());
    }

    private List<Employee> getEmployeesWithManager(String formatQuery) {
        return jdbcTemplate.query(getPreparedStatementCreator(formatQuery), employeeResultSetExtractor);
    }

    private List<Employee> getPageableEmployees(Pageable pageable, List<Employee> employees) {
        int fromIndex = getFromIndex(pageable);
        int toIndex = getToIndex(pageable, employees, fromIndex);
        if (fromIndex > toIndex) {
            return Collections.emptyList();
        } else {
            return Objects.requireNonNull(employees).subList(fromIndex, toIndex);
        }
    }

    private int getToIndex(Pageable pageable, List<Employee> employees, int fromIndex) {
        return Math.min(fromIndex + pageable.getPageSize(), Objects.requireNonNull(employees).size());
    }

    private int getFromIndex(Pageable pageable) {
        return pageable.getPageNumber() * pageable.getPageSize();
    }

    private PreparedStatementCreator getPreparedStatementCreator(String pageableQuery) {
        return connection -> connection.prepareStatement(pageableQuery, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
    }

    private Order getOrder(Pageable pageable) {
        return !pageable.getSort().isEmpty() ? pageable.getSort().toList().get(0) : Order.by(ID);
    }
}