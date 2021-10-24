package com.baitaliuk.company.util;

import com.baitaliuk.company.domain.Employee;
import com.baitaliuk.company.domain.preferences.Change;
import com.baitaliuk.company.domain.preferences.Preference;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Vote {
    public static int majorityDesiredTime(List<Employee> employees) {
        Map<Integer, Integer> votesMap = new HashMap<>();
        for (Employee employee : employees) {
            Preference preference = employee.getPreference();
            if ( preference instanceof Change) {
                int start = preference.getStart();
                votesMap.putIfAbsent(start, 0);
                votesMap.computeIfPresent(start, (key, value) -> (value+1) );
            }
        }
        return findMaxVotes(votesMap);
    }

    public static int findMaxVotes(Map<Integer, Integer> vote) {
        Integer max = vote.values().stream().max(Comparator.naturalOrder()).get();
        return vote.entrySet()
                .stream()
                .filter(entry -> max.equals(entry.getValue()))
                .map(Map.Entry::getKey).collect(Collectors.toList()).get(0);
    }
}
