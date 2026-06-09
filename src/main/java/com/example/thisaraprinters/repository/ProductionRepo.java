package com.example.thisaraprinters.repository;

import com.example.thisaraprinters.model.ProductionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProductionRepo extends JpaRepository<ProductionModel, Integer> {
    Optional<ProductionModel> findByOrderId(String orderId);
}
