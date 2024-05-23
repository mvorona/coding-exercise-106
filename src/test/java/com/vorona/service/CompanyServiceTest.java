package com.vorona.service;

import com.vorona.exception.ApplicationException;
import com.vorona.model.Company;
import com.vorona.model.Employee;
import com.vorona.model.EmployeeDto;
import com.vorona.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CompanyServiceTest {

    private InMemoryEmployeeRepository repository;
    private CompanyService service;

    @BeforeEach
    void setUp() {
        repository = new InMemoryEmployeeRepository();
        service = new CompanyService(repository);
    }

    @Test
    void shouldThrowExceptionIfEmployeeDataIsEmpty() {
        repository.setEmployees(new HashSet<>());

        assertThrows(ApplicationException.class, () -> service.getCompanyHierarchy());
    }

    @Test
    void shouldThrowExceptionIfEmployeeDataIsNull() {
        repository.setEmployees(null);

        assertThrows(ApplicationException.class, () -> service.getCompanyHierarchy());
    }

    @Test
    void shouldReturnCorrectCompanyHierarchy() {
        repository.setEmployees(buildValidRepositoryData());

        Company result = service.getCompanyHierarchy();

        Company expectedResult = getExpectedResult();
        assertEquals(expectedResult, result);
    }

    @Test
    void shouldReturnCorrectCompanyHierarchyIfThereAreDuplicatedEmployees() {
        repository.setEmployees(buildRepositoryDataWithDuplicatedEmployees());

        Company result = service.getCompanyHierarchy();

        Company expectedResult = getCompanyExpectedResultForDuplicates();
        assertEquals(expectedResult, result);
    }

    private static Set<EmployeeDto> buildValidRepositoryData() {
        Set<EmployeeDto> employeeData = new HashSet<>();
        EmployeeDto employeeDto1 = new EmployeeDto("123", "Joe", "Doe", new BigDecimal("60000"), null);
        EmployeeDto employeeDto2 = new EmployeeDto("124", "Martin", "Chekov", new BigDecimal("45000"), "123");
        EmployeeDto employeeDto3 = new EmployeeDto("300", "Alice", "Hasacat", new BigDecimal("50000"), "124");
        EmployeeDto employeeDto4 = new EmployeeDto("301", "Jane", "Doe", new BigDecimal("50000"), "124");
        employeeData.add(employeeDto1);
        employeeData.add(employeeDto2);
        employeeData.add(employeeDto3);
        employeeData.add(employeeDto4);
        return employeeData;
    }

    private static Set<EmployeeDto> buildRepositoryDataWithDuplicatedEmployees() {
        Set<EmployeeDto> employeeData = new HashSet<>();
        EmployeeDto employeeDto1 = new EmployeeDto("123", "Joe", "Doe", new BigDecimal("60000"), null);
        EmployeeDto employeeDto2 = new EmployeeDto("124", "Martin", "Chekov", new BigDecimal("45000"), "123");
        EmployeeDto employeeDto3 = new EmployeeDto("124", "Martin", "Chekov", new BigDecimal("45000"), "123");
        employeeData.add(employeeDto1);
        employeeData.add(employeeDto2);
        employeeData.add(employeeDto3);
        return employeeData;
    }

    private static Company getExpectedResult() {
        Employee employee = new Employee("300", "Alice", "Hasacat", new BigDecimal("50000"), new HashSet<>());
        Employee employee2 = new Employee("301", "Jane", "Doe", new BigDecimal("50000"), new HashSet<>());
        Set<Employee> managerSubordinates = new HashSet<>();
        managerSubordinates.add(employee);
        managerSubordinates.add(employee2);
        Employee manager = new Employee("124", "Martin", "Chekov", new BigDecimal("45000"), managerSubordinates);
        Set<Employee> ceoSubordinates = new HashSet<>();
        ceoSubordinates.add(manager);
        Employee ceo = new Employee("123", "Joe", "Doe", new BigDecimal("60000"), ceoSubordinates);
        return new Company(ceo);
    }

    private static Company getCompanyExpectedResultForDuplicates() {
        Employee employee = new Employee("124", "Martin", "Chekov", new BigDecimal("45000"), new HashSet<>());
        Set<Employee> subordinates = new HashSet<>();
        subordinates.add(employee);
        Employee ceo = new Employee("123", "Joe", "Doe", new BigDecimal("60000"), subordinates);
        return new Company(ceo);
    }
}

class InMemoryEmployeeRepository implements EmployeeRepository {
    private Set<EmployeeDto> employees;

    @Override
    public Set<EmployeeDto> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<EmployeeDto> employees) {
        this.employees = employees;
    }
}
