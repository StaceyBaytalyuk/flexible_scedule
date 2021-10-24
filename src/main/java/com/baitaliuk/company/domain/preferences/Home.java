package com.baitaliuk.company.domain.preferences;

import com.baitaliuk.company.domain.Company;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("3")
public class Home extends Preference {
    public static final double efficiencySatisfied = 1.1;

    public Home() {
        this.name = "home";
        this.start = 0;
    }

    public int getEfficiency(boolean isFlexible, int mustStart) {
        if ( isFlexible ) {
            double result = efficiencySatisfied * Company.workingHours * 100;
            //double result = efficiencySatisfied * Company.workingHours;
            return (int) result;
        } else {
            return Company.workingHours * 100;
            //return Company.workingHours;
        }
    }
}
