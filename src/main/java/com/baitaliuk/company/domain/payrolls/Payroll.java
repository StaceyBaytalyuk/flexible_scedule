package com.baitaliuk.company.domain.payrolls;

import com.baitaliuk.company.domain.Employee;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Payroll")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="payroll_type", discriminatorType = DiscriminatorType.INTEGER)

public abstract class Payroll {
    protected static final int basicHours = 160;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    protected Integer id;

    @Column(name = "name", length = 10)
    protected String name;

    @Column(name = "hours")
    protected Integer hours;

    @OneToMany(fetch = FetchType.EAGER,
            mappedBy = "department",
            cascade = CascadeType.ALL)
    private List<Employee> employees;

    @Override
    public String toString() {
        return name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getHours() {
        return hours;
    }

    public void setHours(Integer hours) {
        this.hours = hours;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }
}
