package com.example.usercredentials.repositories;

import com.example.usercredentials.documents.Employee;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends UserRepository<Employee> {
    Optional<Employee> findEmployeeBySsn(String SSN);
}
