package com.vorona.model;

import java.math.BigDecimal;

public record EmployeeDto(String id, String firstName, String lastName, BigDecimal salary, String managerId) {

    @Override
    public String toString() {
        return "EmployeeDto{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", salary=" + salary +
                ", managerId='" + managerId + '\'' +
                '}';
    }
}
