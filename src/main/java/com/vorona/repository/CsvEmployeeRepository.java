package com.vorona.repository;

import com.vorona.exception.ApplicationException;
import com.vorona.mapper.EmployeeDtoMapper;
import com.vorona.model.EmployeeDto;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Repository implementation for reading employees from a CSV file.
 */
public class CsvEmployeeRepository implements EmployeeRepository {

    private final String sourceFile;

    public static final String COMMA_DELIMITER = ",";

    public CsvEmployeeRepository(String sourceFile) {
        this.sourceFile = sourceFile;
    }

    /**
     * Retrieves a set of employees from the CSV file.
     *
     * @return a set of {@code EmployeeDto} objects representing the employees
     * @throws ApplicationException if an I/O error occurs while reading the CSV file
     */
    @Override
    public Set<EmployeeDto> getEmployees() {
        Set<EmployeeDto> records;
        try (Stream<String> lines = Files.lines(Paths.get(sourceFile)).skip(1)) {
            records = lines.map(line -> Arrays.asList(line.split(COMMA_DELIMITER)))
                    .map(EmployeeDtoMapper::mapEmployeeDto)
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            throw new ApplicationException("Error while loading employees", e);
        }
        return records;
    }

}
