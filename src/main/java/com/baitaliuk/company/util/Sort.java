package com.baitaliuk.company.util;

import com.baitaliuk.company.domain.Employee;
import com.baitaliuk.company.repos.EmployeeRepo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Sort {
    public static List<Employee> sortEmployeesById(List<Employee> employees) {
        return employees.stream().sorted(Comparator.comparing(Employee::getId)).collect(Collectors.toList());
    }

    public static List<Employee> getSortedEmployees(EmployeeRepo employeeRepo) {
        return sortEmployeesById( (ArrayList<Employee>) employeeRepo.findAll() );
    }
}
