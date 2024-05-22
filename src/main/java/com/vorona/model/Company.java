package com.vorona.model;

import java.util.Objects;

public record Company(Employee ceo) {

    @Override
    public String toString() {
        return "{\n" +
                "\"ceo\":" + ceo +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Company company = (Company) o;
        return Objects.equals(ceo, company.ceo);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(ceo);
    }
}
