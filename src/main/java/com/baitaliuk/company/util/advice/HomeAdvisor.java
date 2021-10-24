package com.baitaliuk.company.util.advice;

import com.baitaliuk.company.domain.profitable.Department;
import com.baitaliuk.company.domain.Employee;
import com.baitaliuk.company.domain.profitable.Role;

public class HomeAdvisor implements Advisor {
    public String findSolutions(Employee employee) {
        StringBuilder sb = new StringBuilder();
        Department department = employee.getDepartment();
        Role role = employee.getRole();
        int count = 1;

        if ( !department.isFlexible() ) {
            sb.append(count+") Зробити відділ "+employee.getDepartment().getName()+" гнучким");
            count++;
        }

        if ( !role.isFlexible() ) {
            sb.append("\n"+count+") Зробити посаду "+employee.getRole().getName()+" гнучкою");
            count++;
        }

        if ( department.isSynchronous() ) {
            sb.append("\n"+count+") Зробити відділ "+employee.getDepartment().getName()+" не синхронним");
        }
        return sb.toString();
    }

}
