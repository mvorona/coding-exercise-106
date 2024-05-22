package com.vorona.mapper;

import com.vorona.model.EmployeeDto;

import java.math.BigDecimal;
import java.util.List;

public class EmployeeDtoMapper {

    public static EmployeeDto mapEmployeeDto(List<String> values) {
        String id = values.get(0);
        String firstName = values.get(1);
        String lastName = values.get(2);
        BigDecimal salary = new BigDecimal(values.get(3));
        String managerId = null;
        if (values.size() > 4) {
            managerId = values.get(4);
        }
        return new EmployeeDto(id, firstName, lastName, salary, managerId);
    }
}
