package com.baitaliuk.company.controller;

import com.baitaliuk.company.domain.profitable.Department;
import com.baitaliuk.company.domain.Employee;
import com.baitaliuk.company.domain.profitable.Role;
import com.baitaliuk.company.domain.payrolls.Payroll;
import com.baitaliuk.company.domain.payrolls.Wages;
import com.baitaliuk.company.domain.preferences.Change;
import com.baitaliuk.company.domain.preferences.Preference;
import com.baitaliuk.company.repos.*;
import com.baitaliuk.company.util.Sort;
import com.baitaliuk.company.util.Timetable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
public class EmployeeController {
    private final EmployeeRepo employeeRepo;
    private final PreferenceRepo preferenceRepo;
    private final RoleRepo roleRepo;
    private final DepartmentRepo departmentRepo;
    private final PayrollRepo payrollRepo;

    public EmployeeController(EmployeeRepo employeeRepo, PreferenceRepo preferenceRepo, RoleRepo roleRepo, DepartmentRepo departmentRepo, PayrollRepo payrollRepo) {
        this.employeeRepo = employeeRepo;
        this.preferenceRepo = preferenceRepo;
        this.roleRepo = roleRepo;
        this.departmentRepo = departmentRepo;
        this.payrollRepo = payrollRepo;
    }

    @GetMapping("employees")
    public String employees(Map<String, Object> model) {
        List<Employee> employees = Sort.getSortedEmployees(employeeRepo);
        model.put("employees", employees);
        return "employees";
    }

    @GetMapping("employee_add")
    public String employeeAdd() {
        return "employee_add";
    }

    @GetMapping("register_employee")
    public String registerEmployee(@RequestParam String name, @RequestParam String role, @RequestParam String department,
                                   @RequestParam String preference, @RequestParam Integer preferredStart,
                                   @RequestParam String payroll, @RequestParam Integer hours, Map<String, Object> model) {

        boolean notEmpty = !role.isEmpty() && !department.isEmpty() && !preference.isEmpty() && !payroll.isEmpty();
        if ( notEmpty ) {
            Role r = roleRepo.findByName(role);
            Department d = departmentRepo.findByName(department);
            Preference pref = preferenceRepo.findFirstByName(preference);
            if ( pref instanceof Change ) {
                pref = null;
                if ( preferredStart != null ) {
                    pref = preferenceRepo.findByStart(preferredStart);
                } else {
                    model.put("message", "Якщо побажання change - введіть бажаний час");
                    return employeeAdd();
                }
            }
            Payroll pay = payrollRepo.findFirstByName(payroll);
            if ( pay instanceof Wages ) {
                pay = null;
                if ( hours != null ) {
                    if ( hours > 0 ) {
                        pay = payrollRepo.findFirstByHours(hours);
                        if ( pay == null ) {
                            payrollRepo.save(new Wages(hours));
                        }
                    } else {
                        model.put("message", "Час не може бути від'ємним числом");
                        return employeeAdd();
                    }
                } else {
                    model.put("message", "Введіть кількість годин");
                    return employeeAdd();
                }
            }

            boolean notNull = r!=null && d!=null && pref!=null && pay!=null;
            if ( notNull ) {
                Employee.EmployeeBuilder builder = new Employee.EmployeeBuilder().setName(name).setDepartment(d).setRole(r).setPreference(pref).setPayroll(pay);
                Employee employee = builder.build();
                employeeRepo.save(employee);
                employee = employeeRepo.findById(employeeRepo.findIdOfLastEmployee()).get();

                if ( name.isEmpty() ) {
                    employee.generateName();
                }
                Timetable.setTime(employee);
                employeeRepo.save(employee);
                return employees(model);
            } else {
                model.put("message", "Неправильно введені дані");
            }
        } else {
            model.put("message", "Введіть усі обов'язкові поля - позначено зірочкою (*)");
        }
        return employeeAdd();
    }

}
