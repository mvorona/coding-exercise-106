package com.vorona.service;

import com.vorona.model.Employee;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DefaultSalaryServiceTest {

    private DefaultSalaryService salaryService = new DefaultSalaryService();

    @Test
    void calculateSalaryLowerLimitForManager_shouldReturn0IfEmployeeHasNoSubordinates() {
        Employee employee = new Employee("1", "firstName", "lastName", BigDecimal.TEN, new HashSet<>());

        BigDecimal result = salaryService.calculateSalaryLowerLimitForManager(employee);

        assertEquals(0, result.compareTo(BigDecimal.ZERO));
    }

    @Test
    void calculateSalaryLowerLimitForManager_shouldReturnCorrectLowerLimit() {
        Employee subordinate1 = new Employee("2", "firstName", "lastName", new BigDecimal("100"), new HashSet<>());
        Employee subordinate2 = new Employee("3", "firstName", "lastName", new BigDecimal("200"), new HashSet<>());
        Set<Employee> subordinates = new HashSet<>();
        subordinates.add(subordinate1);
        subordinates.add(subordinate2);
        Employee employee = new Employee("1", "firstName", "lastName", BigDecimal.TEN, subordinates);

        BigDecimal result = salaryService.calculateSalaryLowerLimitForManager(employee);

        assertEquals(new BigDecimal("180.0"), result);
    }

    @Test
    void calculateSalaryUpperLimitForManager_shouldReturn0IfEmployeeHasNoSubordinates() {
        Employee employee = new Employee("1", "firstName", "lastName", BigDecimal.TEN, new HashSet<>());

        BigDecimal result = salaryService.calculateSalaryUpperLimitForManager(employee);

        assertEquals(0, result.compareTo(BigDecimal.ZERO));
    }

    @Test
    void calculateSalaryUpperLimitForManager_shouldReturnCorrectUpperLimit() {
        Employee subordinate1 = new Employee("2", "firstName", "lastName", new BigDecimal("100"), new HashSet<>());
        Employee subordinate2 = new Employee("3", "firstName", "lastName", new BigDecimal("200"), new HashSet<>());
        Set<Employee> subordinates = new HashSet<>();
        subordinates.add(subordinate1);
        subordinates.add(subordinate2);
        Employee employee = new Employee("1", "firstName", "lastName", BigDecimal.TEN, subordinates);

        BigDecimal result = salaryService.calculateSalaryUpperLimitForManager(employee);

        assertEquals(new BigDecimal("225.0"), result);
    }

}