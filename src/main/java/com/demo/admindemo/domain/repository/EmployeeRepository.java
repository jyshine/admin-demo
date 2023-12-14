package com.demo.admindemo.domain.repository;


import com.demo.admindemo.domain.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
