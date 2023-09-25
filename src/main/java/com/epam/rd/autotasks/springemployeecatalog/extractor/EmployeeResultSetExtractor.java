package com.epam.rd.autotasks.springemployeecatalog.extractor;

import com.epam.rd.autotasks.springemployeecatalog.domain.Employee;
import com.epam.rd.autotasks.springemployeecatalog.mappers.MapperComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class EmployeeResultSetExtractor implements ResultSetExtractor<List<Employee>> {
    private final MapperComponent mapperComponent;

    @Autowired
    public EmployeeResultSetExtractor(MapperComponent mapperComponent) {
        this.mapperComponent = mapperComponent;
    }

    @Override
    public List<Employee> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        List<Employee> employees = new ArrayList<>();
        while (resultSet.next()) {
            employees.add(mapperComponent.getEmployee(resultSet, mapperComponent.getManager(resultSet)));
        }
        return employees;
    }
}