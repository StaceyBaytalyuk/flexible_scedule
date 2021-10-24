package com.baitaliuk.company.repos;

import com.baitaliuk.company.domain.profitable.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepo extends CrudRepository<Role, Integer> {
    Role findByName(String name);
}
