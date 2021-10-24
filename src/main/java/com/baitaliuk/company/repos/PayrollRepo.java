package com.baitaliuk.company.repos;

import com.baitaliuk.company.domain.payrolls.Payroll;
import org.springframework.data.repository.CrudRepository;

public interface PayrollRepo extends CrudRepository<Payroll, Integer> {
    Payroll findByName(String name);
    Payroll findFirstByName(String name);
    Payroll findByHours(int hours);
    Payroll findFirstByHours(int hours);
}
