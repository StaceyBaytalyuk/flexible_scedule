package com.baitaliuk.company.domain;
import com.baitaliuk.company.domain.payrolls.Payroll;
import com.baitaliuk.company.domain.preferences.Preference;
import com.baitaliuk.company.domain.profitable.Department;
import com.baitaliuk.company.domain.profitable.Role;

import javax.persistence.*;

@Entity
@Table(name = "Employee")
public class Employee {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "start")
    private Integer start;

    @ManyToOne(fetch = FetchType.EAGER,
            cascade = CascadeType.ALL)
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne(fetch = FetchType.EAGER,
            cascade = CascadeType.ALL)
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne(fetch = FetchType.EAGER,
            cascade = CascadeType.ALL)
    @JoinColumn(name = "preference_id")
    private Preference preference;

    @ManyToOne(fetch = FetchType.EAGER,
            cascade = CascadeType.ALL)
    @JoinColumn(name = "payroll_id")
    private Payroll payroll;

    public Employee(Department department) {
        this.department = department;
    }

    private Employee(EmployeeBuilder builder) {
        this.name = builder.name;
        this.start = builder.start;
        this.department = builder.department;
        this.role = builder.role;
        this.preference = builder.preference;
        this.payroll = builder.payroll;
    }

    @Override
    public String toString() {
        return "employee"+id +
                ", start at " + start +
                ", department " + department.getName() +
                ", role " + role.getName() +
                ", preference: " + preference +
                ", payroll: " + payroll.getName();
    }

    public void generateName() {
        this.name = "employee"+id;
    }

    public Employee() {}

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

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Preference getPreference() {
        return preference;
    }

    public void setPreference(Preference preference) {
        this.preference = preference;
    }

    public Payroll getPayroll() {
        return payroll;
    }

    public void setPayroll(Payroll payroll) {
        this.payroll = payroll;
    }

    public static class EmployeeBuilder {
        private String name = "-";
        private Integer start = Company.start;
        private Department department = null;
        private Role role = null;
        private Preference preference = null;
        private Payroll payroll = null;

        public EmployeeBuilder setName(String name) {
            if ( name != null ) {
                this.name = name;
            }
            return this;
        }

        public EmployeeBuilder setStart(Integer start) {
            if ( start != null ) {
                this.start = start;
            }
            return this;
        }

        public EmployeeBuilder setDepartment(Department department) {
            if ( department != null ) {
                this.department = department;
            }
            return this;
        }

        public EmployeeBuilder setRole(Role role) {
            if ( role != null ) {
                this.role = role;
            }
            return this;
        }

        public EmployeeBuilder setPreference(Preference preference) {
            if ( preference != null ) {
                this.preference = preference;
            }
            return this;
        }

        public EmployeeBuilder setPayroll(Payroll payroll) {
            if ( payroll != null ) {
                this.payroll = payroll;
            }
            return this;
        }

        public Employee build() {
            return new Employee(this);
        }
    }
}
