package com.vorona.service;

import com.vorona.model.Employee;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;

/**
 * Service class for calculating salary limits for managers.
 */
public class SalaryService {

    private static final BigDecimal LOWER_SALARY_LIMIT_PERCENT = new BigDecimal("1.2");
    private static final BigDecimal UPPER_SALARY_LIMIT_PERCENT = new BigDecimal("1.5");

    /**
     * Calculates the lower salary limit for a manager based on the average salary of their direct subordinates.
     *
     * @param manager the manager whose salary limit is to be calculated
     * @return the lower salary limit for the manager
     */
    public BigDecimal calculateSalaryLowerLimitForManager(Employee manager) {
        BigDecimal avgDirectSubordinatesSalary = getAverageDirectSubordinatesSalary(manager);
        return avgDirectSubordinatesSalary.multiply(LOWER_SALARY_LIMIT_PERCENT);
    }

    /**
     * Calculates the upper salary limit for a manager based on the average salary of their direct subordinates.
     *
     * @param manager the manager whose salary limit is to be calculated
     * @return the upper salary limit for the manager
     */
    public BigDecimal calculateSalaryUpperLimitForManager(Employee manager) {
        BigDecimal avgDirectSubordinatesSalary = getAverageDirectSubordinatesSalary(manager);
        return avgDirectSubordinatesSalary.multiply(UPPER_SALARY_LIMIT_PERCENT);
    }

    private static BigDecimal getAverageDirectSubordinatesSalary(Employee manager) {
        Set<Employee> subordinates = manager.subordinates();
        BigDecimal directSubordinatesSalarySum = BigDecimal.ZERO;
        if (!subordinates.isEmpty()) {
            for (Employee child : subordinates) {
                directSubordinatesSalarySum = directSubordinatesSalarySum.add(child.salary());
            }
            return directSubordinatesSalarySum.divide(new BigDecimal(subordinates.size()), RoundingMode.HALF_UP);
        }
        return directSubordinatesSalarySum;
    }
}
