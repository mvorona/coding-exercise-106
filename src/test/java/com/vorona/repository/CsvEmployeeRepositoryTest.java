package com.vorona.repository;

import com.vorona.exception.ApplicationException;
import com.vorona.model.EmployeeDto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CsvEmployeeRepositoryTest {

    @Test
    void shouldThrowExceptionIfFileIsNotFound() {
        CsvEmployeeRepository repository = new CsvEmployeeRepository("not/existing/file");

        assertThrows(ApplicationException.class, repository::getEmployees);
    }

    @Test
    void shouldReturnEmptySetIfFileIsEmpty() {
        CsvEmployeeRepository repository = new CsvEmployeeRepository("src/test/resources/empty_file.csv");

        Set<EmployeeDto> result = repository.getEmployees();

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldSkipFirstLineWhenProcessingFile() {
        CsvEmployeeRepository repository = new CsvEmployeeRepository("src/test/resources/single_employee.csv");

        Set<EmployeeDto> result = repository.getEmployees();

        assertEquals(1, result.size());
    }

    @Test
    void shouldReturnCorrectEmployeesData() {
        CsvEmployeeRepository repository = new CsvEmployeeRepository("src/test/resources/valid_company.csv");

        Set<EmployeeDto> result = repository.getEmployees();

        assertEquals(buildExpectedResult(), result);
    }

    private static Set<EmployeeDto> buildExpectedResult() {
        Set<EmployeeDto> expectedResult = new HashSet<>();
        EmployeeDto employeeDto1 = new EmployeeDto("123", "Joe", "Doe", new BigDecimal("60000"), null);
        EmployeeDto employeeDto2 = new EmployeeDto("124", "Martin", "Chekov", new BigDecimal("45000"), "123");
        expectedResult.add(employeeDto1);
        expectedResult.add(employeeDto2);
        return expectedResult;
    }

}