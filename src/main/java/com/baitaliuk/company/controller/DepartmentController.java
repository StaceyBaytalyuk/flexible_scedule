package com.baitaliuk.company.controller;

import com.baitaliuk.company.domain.Employee;
import com.baitaliuk.company.domain.profitable.Department;
import com.baitaliuk.company.repos.*;
import com.baitaliuk.company.util.Timetable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
public class DepartmentController {
    private final DepartmentRepo departmentRepo;
    private final EmployeeRepo employeeRepo;

    public DepartmentController(DepartmentRepo departmentRepo, EmployeeRepo employeeRepo) {
        this.departmentRepo = departmentRepo;
        this.employeeRepo = employeeRepo;
    }

    @GetMapping("departments")
    public String departments(Map<String, Object> model) {
        List<Department> departments = (List<Department>) departmentRepo.findAll();
        model.put("departments", departments);
        return "departments";
    }

    @GetMapping("department_add")
    public String departmentAdd() {
        return "department_add";
    }

    @GetMapping("register_department")
    public String registerDepartment(@RequestParam String name, @RequestParam Integer start,
                                     @RequestParam(defaultValue = "false") boolean synchronous,
                                     @RequestParam(defaultValue = "false") boolean flexible, Map<String, Object> model) {

        boolean notEmpty = !name.isEmpty() && start != null;
        if (notEmpty) {
            if (Timetable.checkStart(start)) {
                Department d = departmentRepo.findByName(name);
                if (d == null) {
                    Department department = new Department(name, start, flexible, synchronous);
                    departmentRepo.save(department);
                    return departments(model);
                } else {
                    model.put("message", "Відділ з такою назвою вже існує");
                }
            } else {
                model.put("message", "Оберіть початок роботи у діапазоні 0-15 годин");
            }
        } else {
            model.put("message", "Введіть усі поля");
        }
        return departmentAdd();
    }

    @GetMapping("department_info")
    public String departmentInfo(@RequestParam Integer id, Map<String, Object> model) {
        if (id != null && departmentRepo.existsById(id)) {
            Department department = departmentRepo.findById(id).orElse(new Department());
            model.put("department", department);
            return "department_info";
        }
        return departments(model);
    }

    @PostMapping("department_edit")
    public String departmentEdit(@RequestParam Integer id, @RequestParam String name, @RequestParam Integer start,
                             @RequestParam String synchronous, @RequestParam String flexible, Map<String, Object> model) {

        boolean shouldUpdate = false;
        Department department = departmentRepo.findById(id).orElse(new Department());

        if (!name.isEmpty()) {
            department.setName(name);
        } else {
            model.put("message", "Ім'я не може бути пустим");
        }

        if (Timetable.checkStart(start)) {
            department.setStart(start);
            shouldUpdate = true;
        } else {
            model.put("message", "Оберіть початок роботи у діапазоні 0-15 годин");
        }

        boolean s = Boolean.parseBoolean(synchronous);
        boolean f = Boolean.parseBoolean(flexible);
        if ( s != department.isSynchronous() || f != department.isFlexible() ) {
            department.setSynchronous(Boolean.parseBoolean(synchronous));
            department.setFlexible(Boolean.parseBoolean(flexible));
            shouldUpdate = true;
        }

        departmentRepo.save(department);
        if ( shouldUpdate ) {
            updateEmployees(department);
        }
        return departmentInfo(id, model);
    }


    private void updateEmployees(Department department) {
        //List<Employee> employees = department.getEmployees(); // не работает, ConcurrentModificationException
        List<Employee> employees = employeeRepo.findAllByDepartment(department);
        Timetable.setTime(employees);
        employeeRepo.saveAll(employees);
    }

}
