package com.epam.rd.autotasks.springemployeecatalog.constants;

public class SQLQuery {

    public static final String ALL_FROM_EMPLOYEE_FORMAT_QUERY =
            "SELECT e.id, e.firstName, e.lastName, e.middleName, e.position, e.manager, e.hireDate AS hired, e.salary, e.department, d.name, d.location " +
                    "FROM employee e " +
                    "LEFT JOIN employee m on e.manager = m.id " +
                    "LEFT JOIN department d ON e.department = d.id " +
                    "ORDER BY %s %s";

    public static final String EMPLOYEE_BY_ID_FORMAT_QUERY =
            "WITH RECURSIVE cte (id, firstName, lastName, middleName, position, manager, hireDate, salary, department, name, location) AS ( " +
                    "SELECT id, firstName, lastName, middleName, position, manager, hireDate, salary, department " +
                    "FROM employee e WHERE id = %d" +
                    "UNION ALL " +
                    "SELECT m.id, m.firstName, m.lastName, m.middleName, m.position, m.manager, m.hireDate, m.salary, m.department " +
                    "FROM employee m " +
                    "INNER JOIN cte ON cte.manager = m.id " +
                    ") SELECT * FROM cte " +
                    "LEFT JOIN department d ON cte.department = d.id";

    public static final String EMPLOYEES_BY_MANAGER_FORMAT_QUERY =
            "WITH RECURSIVE cte (id, firstName, lastName, middleName, position, manager, hireDate, salary, department, name, location) AS ( " +
                    "SELECT id, firstName, lastName, middleName, position, manager, hireDate, salary, department " +
                    "FROM employee e WHERE manager = %d " +
                    "UNION ALL " +
                    "SELECT m.id, m.firstName, m.lastName, m.middleName, m.position, m.manager, m.hireDate, m.salary, m.department " +
                    "FROM employee m " +
                    "INNER JOIN cte ON cte.manager = m.id " +
                    ") SELECT DISTINCT cte.id, firstName, lastName, middleName, position, manager, hireDate as hired, salary, department, name, location FROM cte " +
                    "LEFT JOIN department d ON cte.department = d.id " +
                    "ORDER BY %s %s";

    public static final String EMPLOYEES_BY_DEP_ID_FORMAT_QUERY =
            "WITH RECURSIVE cte (id, firstName, lastName, middleName, position, manager, hireDate, salary, department, name, location) AS ( " +
                    "SELECT id, firstName, lastName, middleName, position, manager, hireDate, salary, department " +
                    "FROM employee e WHERE department = %d " +
                    "UNION ALL " +
                    "SELECT m.id, m.firstName, m.lastName, m.middleName, m.position, m.manager, m.hireDate, m.salary, m.department " +
                    "FROM employee m " +
                    "INNER JOIN cte ON cte.manager = m.id " +
                    ") SELECT DISTINCT cte.id, firstName, lastName, middleName, position, manager, hireDate as hired, salary, department, name, location FROM cte " +
                    "LEFT JOIN department d ON cte.department = d.id " +
                    "ORDER BY %s %s";

    public static final String EMPLOYEES_BY_DEP_NAME_FORMAT_QUERY =
            "WITH RECURSIVE cte (id, firstName, lastName, middleName, position, manager, hireDate, salary, department, name, location) AS ( " +
                    "SELECT e.id, firstName, lastName, middleName, position, manager, hireDate, salary, department " +
                    "FROM employee e " +
                    "LEFT JOIN department d ON e.department = d.id " +
                    "WHERE d.name = '%s' " +
                    "UNION ALL " +
                    "SELECT m.id, m.firstName, m.lastName, m.middleName, m.position, m.manager, m.hireDate, m.salary, m.department " +
                    "FROM employee m " +
                    "INNER JOIN cte ON cte.manager = m.id " +
                    ") SELECT DISTINCT cte.id, firstName, lastName, middleName, position, manager, hireDate as hired, salary, department, name, location FROM cte " +
                    "LEFT JOIN department d ON cte.department = d.id " +
                    "ORDER BY %s %s";
}