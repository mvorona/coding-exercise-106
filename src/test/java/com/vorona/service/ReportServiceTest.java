package com.vorona.service;

import com.vorona.model.Company;
import com.vorona.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReportServiceTest {

    private TestSalaryService salaryService;
    private ReportService service;

    private Employee ceo;
    private Employee manager;
    private Employee employee;
    private Employee employeeWithLongReportingLine;
    private Company company;

    @BeforeEach
    void setup() {
        salaryService = new TestSalaryService();
        service = new ReportService(salaryService);

        employee = new Employee("300", "Alice", "Hasacat", new BigDecimal("50000"), new HashSet<>());
        Employee employee2 = new Employee("301", "Jane", "Doe", new BigDecimal("50000"), new HashSet<>());
        Set<Employee> managerSubordinates = new HashSet<>();
        managerSubordinates.add(employee);
        managerSubordinates.add(employee2);
        manager = new Employee("224", "Martin", "Chekov", new BigDecimal("45000"), managerSubordinates);
        Set<Employee> ceoSubordinates = new HashSet<>();
        ceoSubordinates.add(manager);
        ceo = new Employee("123", "Joe", "Doe", new BigDecimal("60000"), ceoSubordinates);
        company = new Company(ceo);
    }

    @Test
    void getUnderpaidEmployees_shouldReturnUnderpaidEmployees() {
        salaryService.setLowerLimit(ceo, new BigDecimal("65000"));
        salaryService.setLowerLimit(manager, new BigDecimal("48000"));

        Map<BigDecimal, Set<Employee>> result = service.getUnderpaidEmployees(company);

        assertEquals(2, result.size());
        Set<Employee> entry1Employees = new HashSet<>();
        entry1Employees.add(ceo);
        Map.Entry<BigDecimal, Set<Employee>> entry1 = new AbstractMap.SimpleEntry<>(new BigDecimal("5000"), entry1Employees);
        assertEquals(result.get(entry1.getKey()), entry1.getValue());
        Set<Employee> entry2Employees = new HashSet<>();
        entry2Employees.add(manager);
        Map.Entry<BigDecimal, Set<Employee>> entry2 = new AbstractMap.SimpleEntry<>(new BigDecimal("3000"), entry2Employees);
        assertEquals(result.get(entry2.getKey()), entry2.getValue());
    }

    @Test
    void getUnderpaidEmployees_shouldNotReturnUnderpaidEmployeesIfTheSalaryIsWithinRange() {
        salaryService.setLowerLimit(ceo, new BigDecimal("60000"));
        salaryService.setLowerLimit(manager, new BigDecimal("45000"));

        Map<BigDecimal, Set<Employee>> result = service.getUnderpaidEmployees(company);

        assertEquals(0, result.size());
    }

    @Test
    void getOverpaidEmployees_shouldReturnOverpaidEmployees() {
        salaryService.setUpperLimit(ceo, new BigDecimal("58000"));
        salaryService.setUpperLimit(manager, new BigDecimal("44000"));

        Map<BigDecimal, Set<Employee>> result = service.getOverpaidEmployees(company);

        assertEquals(2, result.size());
        Set<Employee> entry1Employees = new HashSet<>();
        entry1Employees.add(ceo);
        Map.Entry<BigDecimal, Set<Employee>> entry1 = new AbstractMap.SimpleEntry<>(new BigDecimal("2000"), entry1Employees);
        assertEquals(result.get(entry1.getKey()), entry1.getValue());
        Set<Employee> entry2Employees = new HashSet<>();
        entry2Employees.add(manager);
        Map.Entry<BigDecimal, Set<Employee>> entry2 = new AbstractMap.SimpleEntry<>(new BigDecimal("1000"), entry2Employees);
        assertEquals(result.get(entry2.getKey()), entry2.getValue());
    }

    @Test
    void getOverpaidEmployees_shouldNotReturnOverpaidEmployeesIfTheSalaryIsWithinRange() {
        salaryService.setUpperLimit(ceo, new BigDecimal("60000"));
        salaryService.setUpperLimit(manager, new BigDecimal("45000"));

        Map<BigDecimal, Set<Employee>> result = service.getOverpaidEmployees(company);

        assertEquals(0, result.size());
    }

    @Test
    void getLongReportingLines_shouldNotReturnEmployeesIfReportingLinesAreWithinLimits() {
        Map<Integer, Set<Employee>> result = service.getLongReportingLines(company);

        assertEquals(0, result.size());
    }

    @Test
    void getLongReportingLines_shouldReturnEmployeesIfReportingLinesAreTooLong() {
        buildLongReportingLine();

        Map<Integer, Set<Employee>> result = service.getLongReportingLines(company);

        assertEquals(1, result.size());
        Set<Employee> expectedEmployees = new HashSet<>();
        expectedEmployees.add(employeeWithLongReportingLine);
        Map.Entry<Integer, Set<Employee>> expectedEntry = new AbstractMap.SimpleEntry<>(1, expectedEmployees);
        assertEquals(result.get(expectedEntry.getKey()), expectedEntry.getValue());
    }

    private void buildLongReportingLine() {
        Employee employee4 = new Employee("400", "Alice", "Hasacat", new BigDecimal("50000"), new HashSet<>());
        Employee employee5 = new Employee("500", "Alice", "Hasacat", new BigDecimal("50000"), new HashSet<>());
        employeeWithLongReportingLine = new Employee("600", "Alice", "Hasacat", new BigDecimal("50000"), new HashSet<>());
        employee5 = employee5.withAddedSubordinate(employeeWithLongReportingLine);
        employee4 = employee4.withAddedSubordinate(employee5);
        Set<Employee> employeeSubordinates = new HashSet<>();
        employeeSubordinates.add(employee4);
        employee = new Employee("300", "Alice", "Hasacat", new BigDecimal("50000"), employeeSubordinates);
        Employee employee2 = new Employee("301", "Jane", "Doe", new BigDecimal("50000"), new HashSet<>());
        Set<Employee> managerSubordinates = new HashSet<>();
        managerSubordinates.add(employee);
        managerSubordinates.add(employee2);
        manager = new Employee("224", "Martin", "Chekov", new BigDecimal("45000"), managerSubordinates);
        Set<Employee> ceoSubordinates = new HashSet<>();
        ceoSubordinates.add(manager);
        ceo = new Employee("123", "Joe", "Doe", new BigDecimal("60000"), ceoSubordinates);
        company = new Company(ceo);
    }
}

class TestSalaryService implements SalaryService {
    private final Map<Employee, BigDecimal> lowerLimits = new HashMap<>();
    private final Map<Employee, BigDecimal> upperLimits = new HashMap<>();

    void setLowerLimit(Employee employee, BigDecimal limit) {
        lowerLimits.put(employee, limit);
    }

    void setUpperLimit(Employee employee, BigDecimal limit) {
        upperLimits.put(employee, limit);
    }

    @Override
    public BigDecimal calculateSalaryLowerLimitForManager(Employee manager) {
        return lowerLimits.getOrDefault(manager, BigDecimal.ZERO);
    }

    @Override
    public BigDecimal calculateSalaryUpperLimitForManager(Employee manager) {
        return upperLimits.getOrDefault(manager, BigDecimal.ZERO);
    }
}
