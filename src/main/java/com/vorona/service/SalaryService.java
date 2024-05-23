package com.vorona.service;

import com.vorona.model.Employee;

import java.math.BigDecimal;

public interface SalaryService {
    BigDecimal calculateSalaryLowerLimitForManager(Employee manager);

    BigDecimal calculateSalaryUpperLimitForManager(Employee manager);
}
