package com.baitaliuk.company.domain.preferences;

import com.baitaliuk.company.domain.Employee;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Preference")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="preference_type", discriminatorType = DiscriminatorType.INTEGER)

// strategy
//public interface Preference {
public abstract class Preference {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    protected Integer id;

    @Column(name = "name", length = 6)
    protected String name;

    @Column(name = "start")
    protected Integer start;

    @OneToMany(fetch = FetchType.EAGER,
            mappedBy = "preference",
            cascade = CascadeType.ALL)
    private List<Employee> employees;

    public abstract int getEfficiency(boolean isFlexible, int mustStart);

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

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }
}
