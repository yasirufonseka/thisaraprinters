package com.example.thisaraprinters.repository;

import com.example.thisaraprinters.model.CustomerModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepo extends JpaRepository<CustomerModel, Long> {
    boolean existsByName(String name);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);

    boolean existsByNameAndIdNot(String name, Long id);
    boolean existsByEmailAndIdNot(String email, Long id);
    boolean existsByPhoneAndIdNot(String phone, Long id);
}

