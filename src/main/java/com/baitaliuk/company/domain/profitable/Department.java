package com.baitaliuk.company.domain.profitable;

import com.baitaliuk.company.domain.Employee;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Department")
public class Department implements Profitable {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @Column(name = "name", length = 50, unique = true)
    private String name;

    @Column(name = "start")
    private Integer start;

    @Column(name = "isFlexible")
    private Boolean isFlexible;

    @Column(name = "isSynchronous")
    private Boolean isSynchronous;

    @OneToMany(fetch = FetchType.EAGER,
            mappedBy = "department",
            cascade = CascadeType.ALL)
    private List<Employee> employees = new ArrayList<>();

    public Department(String name, int start, boolean isFlexible, boolean isSynchronous) {
        this.name = name;
        this.start = start;
        this.isFlexible = isFlexible;
        this.isSynchronous = isSynchronous;
    }

    @Override
    public String toString() {
        return "Department "+name;
    }

    public Department() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStart() {
        return start;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Boolean isFlexible() {
        return isFlexible;
    }

    public void setFlexible(Boolean flexible) {
        isFlexible = flexible;
    }

    public Boolean isSynchronous() {
        return isSynchronous;
    }

    public void setSynchronous(Boolean synchronous) {
        isSynchronous = synchronous;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }
}
