package com.vorona.service;

import com.vorona.model.Company;
import com.vorona.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.AbstractMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    private SalaryService salaryService;

    @InjectMocks
    private ReportService service;

    private Employee ceo;
    private Employee manager;
    private Employee employee;
    private Employee employeeWithLongReportingLine;
    private Company company;

    @BeforeEach
    void setup() {
        employee = new Employee("300", "Alice", "Hasacat", new BigDecimal("50000"));
        Employee employee2 = new Employee("301", "Jane", "Doe", new BigDecimal("50000"));
        manager = new Employee("224", "Martin", "Chekov", new BigDecimal("45000"));
        manager.addSubordinate(employee);
        manager.addSubordinate(employee2);
        ceo = new Employee("123", "Joe", "Doe", new BigDecimal("60000"));
        ceo.addSubordinate(manager);
        company = new Company(ceo);
    }

    @Test
    void getUnderpaidEmployees_shouldReturnUnderpaidEmployees() {
        when(salaryService.calculateSalaryLowerLimitForManager(ceo)).thenReturn(new BigDecimal("65000"));
        when(salaryService.calculateSalaryLowerLimitForManager(manager)).thenReturn(new BigDecimal("48000"));

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
        when(salaryService.calculateSalaryLowerLimitForManager(ceo)).thenReturn(new BigDecimal("60000"));
        when(salaryService.calculateSalaryLowerLimitForManager(manager)).thenReturn(new BigDecimal("45000"));

        Map<BigDecimal, Set<Employee>> result = service.getUnderpaidEmployees(company);

        assertEquals(0, result.size());
    }

    @Test
    void getOverpaidEmployees_shouldReturnOverpaidEmployees() {
        when(salaryService.calculateSalaryUpperLimitForManager(ceo)).thenReturn(new BigDecimal("58000"));
        when(salaryService.calculateSalaryUpperLimitForManager(manager)).thenReturn(new BigDecimal("44000"));

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
        when(salaryService.calculateSalaryUpperLimitForManager(ceo)).thenReturn(new BigDecimal("60000"));
        when(salaryService.calculateSalaryUpperLimitForManager(manager)).thenReturn(new BigDecimal("45000"));

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
        Employee employee4 = new Employee("400", "Alice", "Hasacat", new BigDecimal("50000"));
        Employee employee5 = new Employee("500", "Alice", "Hasacat", new BigDecimal("50000"));
        employeeWithLongReportingLine = new Employee("600", "Alice", "Hasacat", new BigDecimal("50000"));
        employee.addSubordinate(employee4);
        employee4.addSubordinate(employee5);
        employee5.addSubordinate(employeeWithLongReportingLine);
    }

}