package com.vorona.mapper;

import com.vorona.model.Employee;
import com.vorona.model.EmployeeDto;

import java.util.HashSet;

public class EmployeeMapper {

    public static Employee map(EmployeeDto dto) {
        return new Employee(dto.id(), dto.firstName(), dto.lastName(), dto.salary(), new HashSet<>());
    }
}
