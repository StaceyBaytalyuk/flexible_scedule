package com.baitaliuk.company.util.advice;

import com.baitaliuk.company.domain.profitable.Department;
import com.baitaliuk.company.domain.Employee;
import com.baitaliuk.company.domain.profitable.Role;
import com.baitaliuk.company.domain.preferences.Change;
import com.baitaliuk.company.domain.preferences.Preference;
import com.baitaliuk.company.util.Vote;

public class ChangeAdvisor implements Advisor {
    public String findSolutions(Employee employee) {
        StringBuilder sb = new StringBuilder();
        Preference preference = employee.getPreference();
        Department department = employee.getDepartment();
        Role role = employee.getRole();
        int count = 1;

        if ( !department.isFlexible() ) {
            sb.append(count+") Зробити відділ "+employee.getDepartment().getName()+" гнучким");
            if ( preference instanceof Change) {
                sb.append(" або змінити час відділу на "+employee.getPreference().getStart()+":00.");
            } else {
                sb.append(".");
            }
            count++;
        }

        if ( !role.isFlexible() ) {
            sb.append("\n"+count+") Зробити посаду "+employee.getRole().getName()+" гнучкою");
            if ( preference instanceof Change) {
                sb.append(" або змінити час посади на "+employee.getPreference().getStart()+":00.");
            } else {
                sb.append(".");
            }
            count++;
        }

        if ( department.isSynchronous() ) {
            sb.append("\n"+count+") Зробити відділ "+employee.getDepartment().getName()+" не синхронним");
            int time = Vote.majorityDesiredTime(department.getEmployees());
            sb.append("\nабо змінити час відділу на "+time+":00 (побажання більшості).");
        }
        return sb.toString();
    }

}
