package com.baitaliuk.company.util;

import com.baitaliuk.company.domain.Company;

public class EfficiencyCalculator {
    public static double calculateFlexible(int wantStart, int mustStart, double efficiency) {
        int a, b;
        int desiredFinish = wantStart + Company.workingHours;
        int mustFinish = mustStart + Company.workingHours;

        boolean noIntersection = desiredFinish <= mustStart || mustFinish <= wantStart;
        if ( noIntersection ) {
            return efficiency * Company.workingHours;
        }

        if ( wantStart < mustStart ) {
            a = mustStart - wantStart;
            b = desiredFinish - mustStart;
            return a*efficiency + b;
        } else {
            a = mustFinish - wantStart;
            b = wantStart - mustStart;
            return a + b*efficiency;
        }
    }
}
