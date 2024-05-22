package com.vorona.repository;

import com.vorona.model.EmployeeDto;

import java.util.Set;

public interface EmployeeRepository {

    Set<EmployeeDto> getEmployees();
}
