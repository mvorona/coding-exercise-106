package com.vorona.service;

import com.vorona.model.Company;
import com.vorona.model.Employee;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Function;

/**
 * Service class for generating reports about employees in a company.
 */
public class ReportService {

    private final SalaryService salaryService;

    private static final int REPORTING_LINE_LIMIT = 4;
    private static final int CEO_LEVEL = 0;

    public ReportService(SalaryService salaryService) {
        this.salaryService = salaryService;
    }

    /**
     * Retrieves underpaid employees in the company.
     *
     * @param company the company whose employees are to be analyzed
     * @return a map where the key is the difference between the employee's salary and the calculated lower limit, and the value is a set of underpaid employees
     */
    public Map<BigDecimal, Set<Employee>> getUnderpaidEmployees(Company company) {
        return buildSalaryReport(company, salaryService::calculateSalaryLowerLimitForManager, (salary, salaryLimit) -> salary.compareTo(salaryLimit) < 0);
    }

    /**
     * Retrieves overpaid employees in the company.
     *
     * @param company the company whose employees are to be analyzed
     * @return a map where the key is the difference between the employee's salary and the calculated upper limit, and the value is a set of overpaid employees
     */
    public Map<BigDecimal, Set<Employee>> getOverpaidEmployees(Company company) {
        return buildSalaryReport(company, salaryService::calculateSalaryUpperLimitForManager, (salary, salaryLimit) -> salary.compareTo(salaryLimit) > 0);
    }

    /**
     * Retrieves employees with long reporting lines.
     *
     * @param company the company whose reporting lines are to be analyzed
     * @return a map where the key is the number of levels beyond the reporting line limit and the value is a set of employees at that level
     */
    public Map<Integer, Set<Employee>> getLongReportingLines(Company company) {
        Map<Integer, Set<Employee>> result = new HashMap<>();

        Queue<Employee> employeeQueue = new LinkedList<>();
        Queue<Integer> hierarchyLevelQueue = new LinkedList<>();
        employeeQueue.add(company.ceo());
        hierarchyLevelQueue.add(CEO_LEVEL);

        while (!employeeQueue.isEmpty()) {
            Employee currentEmployee = employeeQueue.poll();
            int currentLevel = hierarchyLevelQueue.poll();

            if (currentLevel > REPORTING_LINE_LIMIT) {
                int reportingLineDiff = currentLevel - REPORTING_LINE_LIMIT;
                if (result.containsKey(reportingLineDiff)) {
                    result.get(reportingLineDiff).add(currentEmployee);
                } else {
                    Set<Employee> employees = new HashSet<>();
                    employees.add(currentEmployee);
                    result.put(reportingLineDiff, employees);
                }
            }

            if (currentEmployee.getSubordinates() != null) {
                for (Employee child : currentEmployee.getSubordinates()) {
                    employeeQueue.add(child);
                    hierarchyLevelQueue.add(currentLevel + 1);
                }
            }
        }
        return result;
    }

    private Map<BigDecimal, Set<Employee>> buildSalaryReport(Company company, Function<Employee, BigDecimal> salaryLimitCalculationFunction, BiPredicate<BigDecimal, BigDecimal> condition) {
        Map<BigDecimal, Set<Employee>> result = new HashMap<>();

        Queue<Employee> queue = new LinkedList<>();
        queue.add(company.ceo());

        while (!queue.isEmpty()) {
            Employee currentEmployee = queue.poll();
            if (!currentEmployee.getSubordinates().isEmpty()) {
                Set<Employee> subordinates = currentEmployee.getSubordinates();
                queue.addAll(subordinates);
                BigDecimal salaryLimit = salaryLimitCalculationFunction.apply(currentEmployee);
                if (condition.test(currentEmployee.getSalary(), salaryLimit)) {
                    BigDecimal salaryDiff = salaryLimit.subtract(currentEmployee.getSalary()).abs();
                    if (result.containsKey(salaryDiff)) {
                        result.get(salaryDiff).add(currentEmployee);
                    } else {
                        Set<Employee> employees = new HashSet<>();
                        employees.add(currentEmployee);
                        result.put(salaryDiff, employees);
                    }
                }
            }
        }
        return result;
    }
}
