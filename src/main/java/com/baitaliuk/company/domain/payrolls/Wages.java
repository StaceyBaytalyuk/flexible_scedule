package com.baitaliuk.company.domain.payrolls;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("1")
public class Wages extends Payroll {
    public Wages(int hours) {
        this.hours = hours;
        name = "wages";
    }

    public Wages() {
        name = "wages";
    }

    @Override
    public String toString() {
        return "wages, " + hours + " hours";
    }
}
