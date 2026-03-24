package com.example.thisaraprinters.repository;

import com.example.thisaraprinters.model.EmployeeModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepo extends JpaRepository<EmployeeModel, Integer> {
    boolean existsByNic(String nic);
}
