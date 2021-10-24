package com.baitaliuk.company.controller;

import com.baitaliuk.company.domain.*;
import com.baitaliuk.company.domain.profitable.*;
import com.baitaliuk.company.util.advice.*;
import com.baitaliuk.company.domain.money.*;
import com.baitaliuk.company.domain.preferences.Home;
import com.baitaliuk.company.domain.preferences.Preference;
import com.baitaliuk.company.repos.*;
import com.baitaliuk.company.util.Sort;
import com.baitaliuk.company.util.Timetable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Controller
public class ProfitController {
    private final EmployeeRepo employeeRepo;
    private final RoleRepo roleRepo;
    private final DepartmentRepo departmentRepo;
    private final PreferenceRepo preferenceRepo;

    private final int normalProfit = Company.profitPerEmployee * Company.workingHours * 100;
    //private final int normalProfit = Company.profitPerEmployee * Company.workingHours;

    public ProfitController(EmployeeRepo employeeRepo, RoleRepo roleRepo, DepartmentRepo departmentRepo, PreferenceRepo preferenceRepo) {
        this.employeeRepo = employeeRepo;
        this.roleRepo = roleRepo;
        this.departmentRepo = departmentRepo;
        this.preferenceRepo = preferenceRepo;
    }

    @GetMapping("profit")
    public String profit() {
        return "profit";
    }

    @PostMapping("home_profit")
    public String homeProfit(Map<String, Object> model) {
        Preference home = preferenceRepo.findByName("home");
        List<Employee> employees = home.getEmployees();
        List<Profit> profit = calculateProfit(employees);
        List<Profit> zeroProfit = profit.stream().filter(p -> p.getDifference()==0).collect(Collectors.toList());
        int money = (int) (normalProfit * Home.efficiencySatisfied);
        int difference = (money - normalProfit) / 100;
        //int difference = money - normalProfit;

        List<Loss> possibleProfit = new ArrayList<>();
        for (Profit p: zeroProfit) {
            possibleProfit.add(new Loss(difference, p.getSource()));
        }
        giveAdvice(possibleProfit, new HomeAdvisor());

        model.put("possibleProfit", possibleProfit);
        model.put("category", "Працівник");
        model.put("totalPossible", (difference * possibleProfit.size()) );
        return profit();
    }

    @PostMapping("loss_employees")
    public String lossEmployees(Map<String, Object> model) {
        List<Employee> employees = Sort.getSortedEmployees(employeeRepo);
        List<Profit> profit = calculateProfit(employees);
        List<Profit> negativeProfit = profit.stream().filter(p -> p.getDifference()<0).collect(Collectors.toList());
        List<Loss> loss = new ArrayList<>();
        int total = 0;

        for (Profit p: negativeProfit) {
            int difference = p.getDifference()*(-1);
            loss.add(new Loss(difference, p.getSource()));
            total += difference;
        }
        giveAdvice(loss, new ChangeAdvisor());

        model.put("loss", loss);
        model.put("totalLoss", total);
        model.put("category", "Працівник");
        return profit();
    }

    @PostMapping("profit_employees")
    public String profitEmployees(Map<String, Object> model) {
        List<Employee> employees = Sort.getSortedEmployees(employeeRepo);
        List<Profit> profit = calculateProfit(employees);
        int total = calculateTotal(profit);
        model.put("profit", profit);
        model.put("category", "Працівник");
        model.put("total", total);
        return profit();
    }

    @PostMapping("profit_roles")
    public String profitRoles(Map<String, Object> model) {
        List<Role> roles = (ArrayList<Role>) roleRepo.findAll();
        List<Profit> profit = new ArrayList<>();
        int total = 0;

        for (Role role : roles) {
            int sum = calculateProfit(role);
            int difference = (sum - (normalProfit * role.getEmployees().size() / 100));
            //int difference = sum - normalProfit * role.getEmployees().size();
            total += difference;
            profit.add(new Profit(sum, difference, role.getName()));
        }

        model.put("profit", profit);
        model.put("category", "Посада");
        model.put("total", total);
        return profit();
    }

    @PostMapping("profit_departments")
    public String profitDepartments(Map<String, Object> model) {
        List<Department> departments = (ArrayList<Department>) departmentRepo.findAll();
        List<Profit> profit = new ArrayList<>();
        int total = 0;

        for (Department department : departments) {
            int sum = calculateProfit(department);
            int difference = (sum - (normalProfit * department.getEmployees().size() / 100));
            //int difference = sum - (normalProfit * department.getEmployees().size());
            total += difference;
            profit.add(new Profit(sum, difference, department.getName()));
        }

        model.put("profit", profit);
        model.put("category", "Відділ");
        model.put("total", total);
        return profit();
    }

    private int calculateProfit(Employee employee) {
        Preference preference = employee.getPreference();
        Department department = employee.getDepartment();
        Role role = employee.getRole();
        boolean isFlexible = role.isFlexible() && department.isFlexible() && !department.isSynchronous();
        int mustStart = Timetable.findMustStart(employee);
        int hours = preference.getEfficiency(isFlexible, mustStart);
        return Company.profitPerEmployee * hours;
    }

    private List<Profit> calculateProfit(List<Employee> employees) {
        List<Profit> profit = new ArrayList<>();
        for (Employee employee : employees) {
            int money = calculateProfit(employee);
            int difference = money - normalProfit;
            profit.add(new Profit(money / 100, difference / 100, employee.getName()));
            //profit.add(new Profit(money, difference, employee.getName()));
        }
        return profit;
    }

    private int calculateProfit(Profitable source) {
        List<Employee> employees = source.getEmployees();
        int sum = 0;
        for (Employee employee : employees) {
            sum += calculateProfit(employee);
        }
        return sum/100;
        //return sum;
    }

    private int calculateTotal(List<Profit> profit) {
        int total = 0;
        for (Profit p : profit) {
            total += p.getDifference();
        }
        return total;
    }

    private void giveAdvice(List<Loss> loss, Advisor advisor) {
        List<Integer> IDs = findIdsOfEmployeesByLoss(loss);
        List<Employee> employees = (ArrayList<Employee>) employeeRepo.findAllById(IDs);
        for (int i = 0; i < employees.size(); i++) {
            String advice = advisor.findSolutions(employees.get(i));
            loss.get(i).setAdvice(advice);
        }
    }

    private List<Integer> findIdsOfEmployeesByLoss(List<Loss> losses) {
        List<Integer> id = new ArrayList<>();
        for (Loss loss : losses) {
            String source = loss.getSource();
            String idString = Pattern.compile("employee").matcher(source).replaceAll("");
            id.add(Integer.parseInt(idString));
        }
        return id;
    }

    private String convertMoneyToString(int money) {
        return money / 100 + "." + money % 100;
    }

}
