package com.vorona.service;

import com.vorona.exception.ApplicationException;
import com.vorona.mapper.EmployeeMapper;
import com.vorona.model.Company;
import com.vorona.model.Employee;
import com.vorona.model.EmployeeDto;
import com.vorona.repository.EmployeeRepository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
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
        buildCompanyHierarchy(ceo, managerToSubordinatesMap);
        return new Company(ceo);
    }

    private Map<String, Set<EmployeeDto>> mapManagerToSubordinates(Set<EmployeeDto> employeesData) {
        Map<String, Set<EmployeeDto>> managerToSubordinatesMap = new HashMap<>();
        employeesData.forEach(employeeDto -> {
            if (managerToSubordinatesMap.containsKey(employeeDto.managerId())) {
                managerToSubordinatesMap.get(employeeDto.managerId()).add(employeeDto);
            } else {
                Set<EmployeeDto> subordinates = new HashSet<>();
                subordinates.add(employeeDto);
                managerToSubordinatesMap.put(employeeDto.managerId(), subordinates);
            }
        });
        return managerToSubordinatesMap;
    }

    private Employee getCeo(Map<String, Set<EmployeeDto>> employeeDataSource) {
        Optional<EmployeeDto> ceoDto = employeeDataSource.get(CEO_MANAGER_ID)
                .stream()
                .findFirst();
        if (ceoDto.isEmpty()) {
            throw new ApplicationException("CEO not found");
        }
        return EmployeeMapper.map(ceoDto.orElse(null));
    }

    private void buildCompanyHierarchy(Employee ceo, Map<String, Set<EmployeeDto>> employeeDataSource) {
        Queue<Employee> employeeQueue = new LinkedList<>();
        employeeQueue.add(ceo);
        while (!employeeQueue.isEmpty()) {
            Employee employee = employeeQueue.poll();
            Set<EmployeeDto> subordinateDtos = employeeDataSource.get(employee.getId());
            if (subordinateDtos != null) {
                Set<Employee> subordinates = subordinateDtos.stream().map(EmployeeMapper::map).collect(Collectors.toSet());
                employee.addSubordinates(subordinates);
                subordinates.forEach(employeeQueue::offer);
            }
        }
    }
}
