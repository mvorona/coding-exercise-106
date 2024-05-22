package com.vorona.service;

import com.vorona.model.Employee;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SalaryServiceTest {

    private SalaryService salaryService = new SalaryService();

    @Test
    void calculateSalaryLowerLimitForManager_shouldReturn0IfEmployeeHasNoSubordinates() {
        Employee employee = new Employee("1", "firstName", "lastName", BigDecimal.TEN);

        BigDecimal result = salaryService.calculateSalaryLowerLimitForManager(employee);

        assertEquals(0, result.compareTo(BigDecimal.ZERO));
    }

    @Test
    void calculateSalaryLowerLimitForManager_shouldReturnCorrectLowerLimit() {
        Employee employee = new Employee("1", "firstName", "lastName", BigDecimal.TEN);
        Employee subordinate1 = new Employee("2", "firstName", "lastName", new BigDecimal("100"));
        Employee subordinate2 = new Employee("3", "firstName", "lastName", new BigDecimal("200"));
        employee.addSubordinate(subordinate1);
        employee.addSubordinate(subordinate2);

        BigDecimal result = salaryService.calculateSalaryLowerLimitForManager(employee);

        assertEquals(new BigDecimal("180.0"), result);
    }

    @Test
    void calculateSalaryUpperLimitForManager_shouldReturn0IfEmployeeHasNoSubordinates() {
        Employee employee = new Employee("1", "firstName", "lastName", BigDecimal.TEN);

        BigDecimal result = salaryService.calculateSalaryUpperLimitForManager(employee);

        assertEquals(0, result.compareTo(BigDecimal.ZERO));
    }

    @Test
    void calculateSalaryUpperLimitForManager_shouldReturnCorrectUpperLimit() {
        Employee employee = new Employee("1", "firstName", "lastName", BigDecimal.TEN);
        Employee subordinate1 = new Employee("2", "firstName", "lastName", new BigDecimal("100"));
        Employee subordinate2 = new Employee("3", "firstName", "lastName", new BigDecimal("200"));
        employee.addSubordinate(subordinate1);
        employee.addSubordinate(subordinate2);

        BigDecimal result = salaryService.calculateSalaryUpperLimitForManager(employee);

        assertEquals(new BigDecimal("225.0"), result);
    }

}