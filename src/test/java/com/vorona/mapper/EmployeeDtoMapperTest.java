package com.vorona.mapper;

import com.vorona.model.EmployeeDto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class EmployeeDtoMapperTest {

    private static final String ID = "123";
    private static final String FIRST_NAME = "Joe";
    private static final String LAST_NAME = "Doe";
    private static final String SALARY = "60000";
    private static final String MANAGER_ID = "managerId";

    @Test
    void shouldSetManagerIdToNullIfItIsMissing() {
        List<String> values = new ArrayList<>();
        values.add(ID);
        values.add(FIRST_NAME);
        values.add(LAST_NAME);
        values.add(SALARY);

        EmployeeDto result = EmployeeDtoMapper.mapEmployeeDto(values);

        assertNull(result.managerId());
    }

    @Test
    void shouldMapValuesCorrectly() {
        List<String> values = new ArrayList<>();
        values.add(ID);
        values.add(FIRST_NAME);
        values.add(LAST_NAME);
        values.add(SALARY);
        values.add(MANAGER_ID);

        EmployeeDto result = EmployeeDtoMapper.mapEmployeeDto(values);
        EmployeeDto expectedResult = new EmployeeDto(ID, FIRST_NAME, LAST_NAME, new BigDecimal(SALARY), MANAGER_ID);

        assertEquals(expectedResult, result);
    }

}