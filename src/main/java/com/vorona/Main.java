package com.vorona;

import com.vorona.model.Company;
import com.vorona.model.Employee;
import com.vorona.repository.CsvEmployeeRepository;
import com.vorona.repository.EmployeeRepository;
import com.vorona.service.CompanyService;
import com.vorona.service.ReportService;
import com.vorona.service.SalaryService;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        // Configure classes
        EmployeeRepository repository = new CsvEmployeeRepository("src/main/resources/big_company.csv");
        CompanyService companyService = new CompanyService(repository);
        SalaryService salaryService = new SalaryService();
        ReportService reportService = new ReportService(salaryService);

        Company company = companyService.getCompanyHierarchy();
        System.out.println("=============LONG REPORTING LINES REPORT=============");
        Map<Integer, Set<Employee>> longReportingLinesReport = reportService.getLongReportingLines(company);
        for (Map.Entry<Integer, Set<Employee>> entry : longReportingLinesReport.entrySet()) {
            System.out.printf("Employees with these ids %s exceeded reporting line length by %s%n", entry.getValue().stream().map(Employee::id).toList(), entry.getKey());
        }
        System.out.println("=============UNDERPAID EMPLOYEES REPORT=============");
        Map<BigDecimal, Set<Employee>> underpaidEmployeesReport = reportService.getUnderpaidEmployees(company);
        for (Map.Entry<BigDecimal, Set<Employee>> entry : underpaidEmployeesReport.entrySet()) {
            System.out.printf("Employees with these ids %s are underpaid by %s%n", entry.getValue().stream().map(Employee::id).toList(), entry.getKey());
        }
        System.out.println("=============OVERPAID EMPLOYEES REPORT=============");
        Map<BigDecimal, Set<Employee>> overpaidEmployeesReport = reportService.getOverpaidEmployees(company);
        for (Map.Entry<BigDecimal, Set<Employee>> entry : overpaidEmployeesReport.entrySet()) {
            System.out.printf("Employees with these ids %s are overpaid by %s%n", entry.getValue().stream().map(Employee::id).toList(), entry.getKey());
        }
    }
}