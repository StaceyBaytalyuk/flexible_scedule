package com.baitaliuk.company.domain.profitable;

import com.baitaliuk.company.domain.Employee;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Role")
public class Role implements Profitable {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @Column(name = "name", length = 50, unique = true)
    private String name;

    @Column(name = "start")
    private Integer start;

    @Column(name = "isFlexible")
    private Boolean isFlexible;

    @OneToMany(fetch = FetchType.EAGER,
            mappedBy = "role",
            cascade = CascadeType.ALL)
    private List<Employee> employees = new ArrayList<>();

    public Role(String name, int start, boolean isFlexible) {
        this.name = name;
        this.start = start;
        this.isFlexible = isFlexible;
    }

    public Role() {}

    @Override
    public String toString() {
        return "Role "+name;
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

    public Integer getStart() {
        return start;
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

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role)) return false;

        Role role = (Role) o;

        return name.equals(role.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
