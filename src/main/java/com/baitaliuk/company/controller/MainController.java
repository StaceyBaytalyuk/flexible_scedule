package com.baitaliuk.company.controller;

import com.baitaliuk.company.domain.*;
import com.baitaliuk.company.domain.payrolls.Commission;
import com.baitaliuk.company.domain.payrolls.Payroll;
import com.baitaliuk.company.domain.payrolls.Salary;
import com.baitaliuk.company.domain.payrolls.Wages;
import com.baitaliuk.company.domain.preferences.Change;
import com.baitaliuk.company.domain.preferences.Home;
import com.baitaliuk.company.domain.preferences.Preference;
import com.baitaliuk.company.domain.preferences.Same;
import com.baitaliuk.company.domain.profitable.Department;
import com.baitaliuk.company.domain.profitable.Role;
import com.baitaliuk.company.repos.*;
import com.baitaliuk.company.util.Sort;
import com.baitaliuk.company.util.Timetable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;

@Controller
public class MainController {
    private final EmployeeRepo employeeRepo;
    private final PreferenceRepo preferenceRepo;
    private final RoleRepo roleRepo;
    private final DepartmentRepo departmentRepo;
    private final PayrollRepo payrollRepo;

    public MainController(EmployeeRepo employeeRepo, PreferenceRepo preferenceRepo, RoleRepo roleRepo, DepartmentRepo departmentRepo, PayrollRepo payrollRepo) {
        this.employeeRepo = employeeRepo;
        this.preferenceRepo = preferenceRepo;
        this.roleRepo = roleRepo;
        this.departmentRepo = departmentRepo;
        this.payrollRepo = payrollRepo;
    }

    @GetMapping("main_menu")
    public String mainMenu() {
        return "main_menu";
    }

    @GetMapping("initialize_database")
    public String initializeDatabase() {
        return "initialize_database";
    }

    @GetMapping("generate_tables")
    public String generateTables() {
        generateIndependent();
        generateEmployeeAndConnectTables();
        Timetable.setDesirableStart(employeeRepo);
        //setDesirableStart();
        return initializeDatabase();
    }

    private void generateIndependent() {
        List<Department> departments = new ArrayList<>();
        departments.add(new Department("Development", Company.start, true, true));
        departments.add(new Department("Finance", Company.start, true, false));
        departments.add(new Department("HR", 10, false, true));
        departments.add(new Department("Marketing", 12, false, false));
        departments.add(new Department("Administration", Company.start, true, false));
        departmentRepo.saveAll(departments);

        Set<Role> roles = new HashSet<>();
        roles.add(new Role("Team Lead", Company.start, true));
        roles.add(new Role("Developer", Company.start, true));
        roles.add(new Role("Accountant", 9, false));
        roles.add(new Role("Business Analyst", Company.start, true));
        roles.add(new Role("HR Manager", Company.start, true));
        roles.add(new Role("Salesman", 12, false));
        roles.add(new Role("Director", Company.start, true));
        roleRepo.saveAll(roles);

        List<Preference> preferences = new ArrayList<>();
        preferences.add(new Same());
        preferences.add(new Home());
        for (int i = 0; i < 16; i++) {
            preferences.add(new Change(i));
        }
        preferenceRepo.saveAll(preferences);

        List<Payroll> payrolls = new ArrayList<>();
        payrolls.add(new Commission());
        payrolls.add(new Salary());
        payrolls.add(new Wages(150));
        payrolls.add(new Wages(170));
        payrollRepo.saveAll(payrolls);
    }

    private void generateEmployeeAndConnectTables() {
        List<Employee> employees = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            employees.add(new Employee());
        }
        employeeRepo.saveAll(employees);

        employees = Sort.getSortedEmployees(employeeRepo);
        for (int i = 0; i < 11; i++) {
            employees.get(i).setName("employee"+employees.get(i).getId());
        }
        employeeRepo.saveAll(employees);

        employees = Sort.getSortedEmployees(employeeRepo);
        employees.get(0).setDepartment(departmentRepo.findByName("Development"));
        employees.get(1).setDepartment(departmentRepo.findByName("Development"));
        employees.get(2).setDepartment(departmentRepo.findByName("Development"));

        employees.get(3).setDepartment(departmentRepo.findByName("Finance"));
        employees.get(4).setDepartment(departmentRepo.findByName("Finance"));

        employees.get(5).setDepartment(departmentRepo.findByName("HR"));
        employees.get(6).setDepartment(departmentRepo.findByName("HR"));

        employees.get(7).setDepartment(departmentRepo.findByName("Marketing"));
        employees.get(8).setDepartment(departmentRepo.findByName("Marketing"));
        employees.get(9).setDepartment(departmentRepo.findByName("Marketing"));

        employees.get(10).setDepartment(departmentRepo.findByName("Administration"));
        employeeRepo.saveAll(employees);

        employees = Sort.getSortedEmployees(employeeRepo);
        employees.get(0).setRole(roleRepo.findByName("Team Lead"));
        employees.get(1).setRole(roleRepo.findByName("Developer"));
        employees.get(2).setRole(roleRepo.findByName("Developer"));
        employees.get(3).setRole(roleRepo.findByName("Accountant"));
        employees.get(4).setRole(roleRepo.findByName("Business Analyst"));
        employees.get(5).setRole(roleRepo.findByName("HR Manager"));
        employees.get(6).setRole(roleRepo.findByName("HR Manager"));
        employees.get(7).setRole(roleRepo.findByName("Salesman"));
        employees.get(8).setRole(roleRepo.findByName("Salesman"));
        employees.get(9).setRole(roleRepo.findByName("Salesman"));
        employees.get(10).setRole(roleRepo.findByName("Director"));
        employeeRepo.saveAll(employees);

        Preference same = preferenceRepo.findByName("same");
        Preference home = preferenceRepo.findByName("home");
        Preference morning6 = preferenceRepo.findByStart(6);
        Preference morning7 = preferenceRepo.findByStart(7);
        Preference morning9 = preferenceRepo.findByStart(9);
        Preference afternoon13 = preferenceRepo.findByStart(13);

        employees = Sort.getSortedEmployees(employeeRepo);
        employees.get(0).setPreference(same);
        employees.get(1).setPreference(home);
        employees.get(2).setPreference(morning7);

        employees.get(3).setPreference(home);
        employees.get(4).setPreference(morning6);

        employees.get(5).setPreference(home);
        employees.get(6).setPreference(morning9);

        employees.get(7).setPreference(same);
        employees.get(8).setPreference(home);
        employees.get(9).setPreference(afternoon13);

        employees.get(10).setPreference(home);
        employeeRepo.saveAll(employees);


        Payroll commission = payrollRepo.findByName("commission");
        Payroll salary = payrollRepo.findByName("salary");
        Payroll wages = payrollRepo.findByHours(150);

        employees = Sort.getSortedEmployees(employeeRepo);
        employees.get(0).setPayroll(wages);
        employees.get(1).setPayroll(salary);
        employees.get(2).setPayroll(salary);

        employees.get(3).setPayroll(salary);
        employees.get(4).setPayroll(commission);

        employees.get(5).setPayroll(salary);
        employees.get(6).setPayroll(salary);

        employees.get(7).setPayroll(commission);
        employees.get(8).setPayroll(commission);
        employees.get(9).setPayroll(commission);

        employees.get(10).setPayroll(wages);
        employeeRepo.saveAll(employees);
    }

    @GetMapping("normal_start")
    public String normalStart() {
        //setNormalStart();
        Timetable.setNormalStart(employeeRepo);
        return initializeDatabase();
    }

    @GetMapping("preferred_start")
    public String preferredStart() {
        //setDesirableStart();
        Timetable.setDesirableStart(employeeRepo);
        return initializeDatabase();
    }

//    private void setNormalStart() {
//        List<Employee> employees = Sort.getSortedEmployees(employeeRepo);
//        Timetable.seNormalStart(employees);
//        employeeRepo.saveAll(employees);
//    }
//
//    public void setDesirableStart() {
//        List<Employee> employees = Sort.getSortedEmployees(employeeRepo);
//        Timetable.setTime(employees);
//        employeeRepo.saveAll(employees);
//    }

}
