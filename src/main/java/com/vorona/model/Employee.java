package com.vorona.model;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public record Employee(String id, String firstName, String lastName, BigDecimal salary, Set<Employee> subordinates) {

    public Employee {
        subordinates = subordinates == null ? Set.of() : Set.copyOf(subordinates);
    }

    public Employee withAddedSubordinate(Employee subordinate) {
        Set<Employee> newSubordinates = new HashSet<>(this.subordinates);
        newSubordinates.add(subordinate);
        return new Employee(id, firstName, lastName, salary, newSubordinates);
    }

    public Employee withAddedSubordinates(Set<Employee> subordinates) {
        Set<Employee> newSubordinates = new HashSet<>(this.subordinates);
        newSubordinates.addAll(subordinates);
        return new Employee(id, firstName, lastName, salary, newSubordinates);
    }

    @Override
    public String toString() {
        return "{\n" +
                "        \"id\": \"" + id + "\",\n" +
                "        \"firstName\": \"" + firstName + "\",\n" +
                "        \"lastName\": \"" + lastName + "\",\n" +
                "        \"salary\": \"" + salary + "\",\n" +
                "        \"subordinates\": " + subordinates + "\n" +
                "}";
    }
}
