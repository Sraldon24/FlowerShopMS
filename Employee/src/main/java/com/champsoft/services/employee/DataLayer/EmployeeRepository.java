package com.champsoft.services.employee.DataLayer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {


    Optional<Employee> findByEmployeeIdentifier(String employeeIdentifier);
}

