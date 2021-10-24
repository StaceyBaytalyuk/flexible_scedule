package com.baitaliuk.company.controller;

import com.baitaliuk.company.domain.Employee;
import com.baitaliuk.company.domain.profitable.Role;
import com.baitaliuk.company.repos.*;
import com.baitaliuk.company.util.Timetable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class RoleController {
    private final RoleRepo roleRepo;
    private final EmployeeRepo employeeRepo;

    public RoleController(RoleRepo roleRepo, EmployeeRepo employeeRepo) {
        this.roleRepo = roleRepo;
        this.employeeRepo = employeeRepo;
    }

    @GetMapping("roles")
    public String roles(Map<String, Object> model) {
        List<Role> roles = (List<Role>) roleRepo.findAll();
        model.put("roles", roles);
        return "roles";
    }

    @PostMapping("role_add")
    public String roleAdd(@RequestParam String name, @RequestParam Integer start,
                          @RequestParam(defaultValue = "false") boolean flexible, Map<String, Object> model) {

        boolean notEmpty = !name.isEmpty() && start != null;
        if (notEmpty) {
            if ( Timetable.checkStart(start) ) {
                Role r = roleRepo.findByName(name);
                if (r == null) {
                    Role role = new Role(name, start, flexible);
                    roleRepo.save(role);
                } else {
                    model.put("message", "Посада з такою назвою вже існує");
                }
            } else {
                model.put("message", "Оберіть початок роботи у діапазоні 0-15 годин");
            }
        } else {
            model.put("message", "Введіть усі поля");
        }
        return roles(model);
    }

    @PostMapping("role_edit")
    public String roleEdit(@RequestParam Integer id, @RequestParam String name, @RequestParam Integer start,
                           @RequestParam String flexible, Map<String, Object> model) {

        boolean shouldUpdate = false;
        Optional<Role> optional = roleRepo.findById(id);
        if ( optional.isPresent() ) {
            Role role = optional.get();
            if ( !name.isEmpty() ) {
                role.setName(name);
            }

            if ( start!=null ) {
                if ( Timetable.checkStart(start) ) {
                    role.setStart(start);
                    shouldUpdate = true;
                } else {
                    model.put("message", "Оберіть початок роботи у діапазоні 0-15 годин");
                }
            }

            if ( flexible.equals("true") ) {
                role.setFlexible(true);
                shouldUpdate = true;
            } else if ( flexible.equals("false") ) {
                role.setFlexible(false);
                shouldUpdate = true;
            }

            roleRepo.save(role);
            if ( shouldUpdate ) {
                updateEmployees(role);
            }
        } else {
            model.put("message", "Посада з таким id не існує");
        }
        return roles(model);
    }

    private void updateEmployees(Role role) {
        List<Employee> employees = employeeRepo.findAllByRole(role);
        Timetable.setTime(employees);
        employeeRepo.saveAll(employees);
    }
}
