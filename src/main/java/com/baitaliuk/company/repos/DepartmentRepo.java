package com.baitaliuk.company.repos;

import com.baitaliuk.company.domain.profitable.Department;
import org.springframework.data.repository.CrudRepository;

public interface DepartmentRepo extends CrudRepository<Department, Integer> {
    Department findByName(String name);
}
