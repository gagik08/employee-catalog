package com.epam.rd.autotasks.springemployeecatalog.mappers;

import com.epam.rd.autotasks.springemployeecatalog.domain.Department;
import com.epam.rd.autotasks.springemployeecatalog.domain.Employee;
import com.epam.rd.autotasks.springemployeecatalog.domain.FullName;
import com.epam.rd.autotasks.springemployeecatalog.domain.Position;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import static com.epam.rd.autotasks.springemployeecatalog.constants.Constant.DEPARTMENT;
import static com.epam.rd.autotasks.springemployeecatalog.constants.Constant.FIRST_NAME;
import static com.epam.rd.autotasks.springemployeecatalog.constants.Constant.HIRE_DATE;
import static com.epam.rd.autotasks.springemployeecatalog.constants.Constant.ID;
import static com.epam.rd.autotasks.springemployeecatalog.constants.Constant.LAST_NAME;
import static com.epam.rd.autotasks.springemployeecatalog.constants.Constant.LOCATION;
import static com.epam.rd.autotasks.springemployeecatalog.constants.Constant.MANAGER;
import static com.epam.rd.autotasks.springemployeecatalog.constants.Constant.MIDDLE_NAME;
import static com.epam.rd.autotasks.springemployeecatalog.constants.Constant.NAME;
import static com.epam.rd.autotasks.springemployeecatalog.constants.Constant.POSITION;
import static com.epam.rd.autotasks.springemployeecatalog.constants.Constant.SALARY;
import static java.sql.Types.NULL;

@Component
public class MapperComponent {

    public Employee getEmployee(ResultSet resultSet, Employee manager) throws SQLException {
        Long id = resultSet.getLong(ID);
        FullName fullName = getFullName(resultSet);
        Position position = Position.valueOf(resultSet.getString(POSITION));
        LocalDate hired = resultSet.getDate(HIRE_DATE).toLocalDate();
        BigDecimal salary = resultSet.getBigDecimal(SALARY);
        Employee mgr = resultSet.getLong(MANAGER) == NULL ? null : manager;
        Department department = resultSet.getLong(DEPARTMENT) == NULL ? null : getDepartment(resultSet);
        return new Employee(id, fullName, position, hired, salary, mgr, department);
    }

    public Employee getManager(ResultSet resultSet) throws SQLException {
        int currentRow = resultSet.getRow();
        long managerId = resultSet.getLong(MANAGER);
        resultSet.beforeFirst();
        Employee manager = null;
        while (resultSet.next()) {
            if (resultSet.getLong(ID) == managerId) {
                manager = getEmployee(resultSet, null);
                break;
            }
        }
        resultSet.absolute(currentRow);
        return manager;
    }

    public Employee getManagersManager(ResultSet resultSet) throws SQLException {
        int currentRow = resultSet.getRow();
        long managerId = resultSet.getLong(MANAGER);
        resultSet.beforeFirst();
        Employee manager = null;
        while (resultSet.next()) {
            if (resultSet.getLong(ID) == managerId) {
                manager = getEmployee(resultSet, getManagersManager(resultSet));
                break;
            }
        }
        resultSet.absolute(currentRow);
        return manager;
    }

    public FullName getFullName(ResultSet resultSet) throws SQLException {
        String firstName = resultSet.getString(FIRST_NAME);
        String lastName = resultSet.getString(LAST_NAME);
        String middleName = resultSet.getString(MIDDLE_NAME);
        return new FullName(firstName, lastName, middleName);
    }

    public Department getDepartment(ResultSet resultSet) throws SQLException {
        Long depId = resultSet.getLong(DEPARTMENT);
        String name = resultSet.getString(NAME);
        String location = resultSet.getString(LOCATION);
        return new Department(depId, name, location);
    }
}