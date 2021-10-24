package com.baitaliuk.company.repos;

import com.baitaliuk.company.domain.Employee;
import com.baitaliuk.company.domain.profitable.Department;
import com.baitaliuk.company.domain.profitable.Role;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EmployeeRepo extends CrudRepository<Employee, Integer> {
    @Query("SELECT MAX(id) as max_id FROM Employee ")
    Integer findIdOfLastEmployee();

    List<Employee> findAllByDepartment(Department department);

    List<Employee> findAllByRole(Role role);
}
