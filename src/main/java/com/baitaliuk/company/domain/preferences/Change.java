package com.baitaliuk.company.domain.preferences;

import com.baitaliuk.company.util.EfficiencyCalculator;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("1")
public class Change extends Preference {
    public static final double efficiencySatisfied = 1.2;
    public static final double efficiencyUnsatisfied = 0.8;

    public Change(int desiredStart) {
        this.name = "change";
        this.start = desiredStart;
    }

    public Change() {
        this.name = "change";
    }

    public int getEfficiency(boolean isFlexible, int mustStart) {
        int res = 100;
        //int res = 0;
        if ( isFlexible ) {
            res *= EfficiencyCalculator.calculateFlexible(start, mustStart, efficiencySatisfied);
        } else {
            res *= EfficiencyCalculator.calculateFlexible(start, mustStart, efficiencyUnsatisfied);
        }
        return res;
    }

    @Override
    public String toString() {
        return name + " to " + start+":00";
    }
}
