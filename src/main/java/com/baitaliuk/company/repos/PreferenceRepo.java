package com.baitaliuk.company.repos;

import com.baitaliuk.company.domain.preferences.Preference;
import org.springframework.data.repository.CrudRepository;

public interface PreferenceRepo extends CrudRepository<Preference, Integer> {
    Preference findByName(String name);
    Preference findFirstByName(String name);
    Preference findByStart(int start);
}
