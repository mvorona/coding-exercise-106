package com.vorona.model;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Employee {
    private final String id;
    private final String firstName;
    private final String lastName;
    private final BigDecimal salary;
    private final Set<Employee> subordinates;

    public Employee(String id, String firstName, String lastName, BigDecimal salary) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.salary = salary;
        this.subordinates = new HashSet<>();
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public Set<Employee> getSubordinates() {
        return new HashSet<>(subordinates);
    }

    public void addSubordinate(Employee subordinate) {
        this.subordinates.add(subordinate);
    }

    public void addSubordinates(Set<Employee> subordinates) {
        this.subordinates.addAll(subordinates);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(id, employee.id) && Objects.equals(firstName, employee.firstName) && Objects.equals(lastName, employee.lastName) && Objects.equals(salary, employee.salary) && Objects.equals(subordinates, employee.subordinates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, salary, subordinates);
    }
}
