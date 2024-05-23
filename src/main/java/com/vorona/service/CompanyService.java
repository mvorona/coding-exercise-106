package com.vorona.service;

import com.vorona.exception.ApplicationException;
import com.vorona.mapper.EmployeeMapper;
import com.vorona.model.Company;
import com.vorona.model.Employee;
import com.vorona.model.EmployeeDto;
import com.vorona.repository.EmployeeRepository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service class for managing company hierarchy.
 */
public class CompanyService {

    public CompanyService(EmployeeRepository repository) {
        this.repository = repository;
    }

    private static final Object CEO_MANAGER_ID = null;

    private final EmployeeRepository repository;

    /**
     * Retrieves the company hierarchy starting from the CEO.
     *
     * @return the {@code Company} object representing the company hierarchy
     * @throws ApplicationException if no employees are found or if the CEO is not found
     */
    public Company getCompanyHierarchy() {
        Set<EmployeeDto> employeesData = repository.getEmployees();
        if (employeesData == null || employeesData.isEmpty()) {
            throw new ApplicationException("No employees found");
        }
        Map<String, Set<EmployeeDto>> managerToSubordinatesMap = mapManagerToSubordinates(employeesData);
        Employee ceo = getCeo(managerToSubordinatesMap);
        ceo = buildCompanyHierarchy(ceo, managerToSubordinatesMap);
        return new Company(ceo);
    }

    private Map<String, Set<EmployeeDto>> mapManagerToSubordinates(Set<EmployeeDto> employeesData) {
        Map<String, Set<EmployeeDto>> managerToSubordinatesMap = new HashMap<>();
        employeesData.forEach(employeeDto -> {
            managerToSubordinatesMap
                    .computeIfAbsent(employeeDto.managerId(), k -> new HashSet<>())
                    .add(employeeDto);
        });
        return managerToSubordinatesMap;
    }

    private Employee getCeo(Map<String, Set<EmployeeDto>> employeeDataSource) {
        Optional<EmployeeDto> ceoDto = Optional.ofNullable(employeeDataSource.get(CEO_MANAGER_ID))
                .flatMap(set -> set.stream().findFirst());
        if (ceoDto.isEmpty()) {
            throw new ApplicationException("CEO not found");
        }
        return EmployeeMapper.map(ceoDto.orElse(null));
    }

    private Employee buildCompanyHierarchy(Employee employee, Map<String, Set<EmployeeDto>> employeeDataSource) {
        Set<EmployeeDto> subordinateDtos = employeeDataSource.get(employee.id());
        if (subordinateDtos == null || subordinateDtos.isEmpty()) {
            return employee;
        }

        Set<Employee> subordinates = subordinateDtos.stream()
                .map(EmployeeMapper::map)
                .map(subordinate -> buildCompanyHierarchy(subordinate, employeeDataSource))
                .collect(Collectors.toSet());

        return employee.withAddedSubordinates(subordinates);
    }
}
