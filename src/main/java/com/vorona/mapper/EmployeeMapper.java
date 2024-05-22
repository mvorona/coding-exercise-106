package com.vorona.mapper;

import com.vorona.model.Employee;
import com.vorona.model.EmployeeDto;

public class EmployeeMapper {

    public static Employee map(EmployeeDto dto) {
        return new Employee(dto.id(), dto.firstName(), dto.lastName(), dto.salary());
    }
}
