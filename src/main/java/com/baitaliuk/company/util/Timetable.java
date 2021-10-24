package com.baitaliuk.company.util;

import com.baitaliuk.company.domain.profitable.Department;
import com.baitaliuk.company.domain.Employee;
import com.baitaliuk.company.domain.profitable.Role;
import com.baitaliuk.company.repos.DepartmentRepo;
import com.baitaliuk.company.repos.EmployeeRepo;

import java.util.Collections;
import java.util.List;

public class Timetable {
    public static int findMustStart(Employee employee) {
        Role role = employee.getRole();
        Department department = employee.getDepartment();
        if ( !department.isFlexible() ) {
            return department.getStart();
        } else if ( !role.isFlexible() ) {
            return role.getStart();
        } else {
            if ( !department.isSynchronous() ) {
                return role.getStart();
            } else {
                return department.getStart();
            }
        }
    }

    /**
     * @return true if allowed to change, false if not allowed
     */
    public static boolean setPreferredStartIfAllowed(Employee employee) {
        int wantStart = employee.getPreference().getStart();
        Department department = employee.getDepartment();
        Role role = employee.getRole();

        boolean isFlexible = department.isFlexible() && !department.isSynchronous() && role.isFlexible();
        if ( isFlexible ) {
            employee.setStart(wantStart);
            return true;
        }
        else return false;
    }

    public static void setTime(Employee employee) {
        if ( !setPreferredStartIfAllowed(employee) ) {
            employee.setStart(findMustStart(employee));
        }
    }

    public static void setTime(List<Employee> employees) {
        for (int i = 0; i < employees.size(); i++) {
            setTime(employees.get(i));
        }
    }

    public static void seNormalStart(List<Employee> employees) {
        for (int i = 0; i < employees.size(); i++) {
            Employee employee = employees.get(i);
            int mustStart = findMustStart(employee);
            employee.setStart(mustStart);
        }
    }

    public static boolean checkStart(int start) {
        return start >= 0 && start <= 15;
    }


    public static void setNormalStart(EmployeeRepo employeeRepo) {
        List<Employee> employees = Sort.getSortedEmployees(employeeRepo);
        Timetable.seNormalStart(employees);
        employeeRepo.saveAll(employees);
    }

    public static void setDesirableStart(EmployeeRepo employeeRepo) {
        List<Employee> employees = Sort.getSortedEmployees(employeeRepo);
        Timetable.setTime(employees);
        employeeRepo.saveAll(employees);
    }
}
