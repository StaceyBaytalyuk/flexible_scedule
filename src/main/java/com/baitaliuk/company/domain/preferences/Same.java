package com.baitaliuk.company.domain.preferences;

import com.baitaliuk.company.domain.Company;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("2")
public class Same extends Preference {
    public Same() {
        this.name = "same";
        this.start = 0;
    }

    public int getEfficiency(boolean isFlexible, int mustStart) {
        return Company.workingHours * 100;
        //return Company.workingHours;
    }

}
