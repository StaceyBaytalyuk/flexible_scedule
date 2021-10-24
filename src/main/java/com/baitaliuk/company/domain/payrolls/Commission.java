package com.baitaliuk.company.domain.payrolls;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("3")
public class Commission extends Payroll {
    private static int percent = 5;

    public Commission() {
        hours = basicHours;
        name = "commission";
    }

    public static int getPercent() {
        return percent;
    }

    public static void setPercent(int newPercent) {
        percent = newPercent;
    }

    @Override
    public String toString() {
        return "commission (basic salary + " + percent + "% from sales)";
    }
}
