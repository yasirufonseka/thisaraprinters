package com.example.thisaraprinters.repository;

import com.example.thisaraprinters.model.QuotationModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuotationRepo extends JpaRepository<QuotationModel,Integer> {
}
