package com.example.thisaraprinters.repository;

import com.example.thisaraprinters.model.MaterialVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MaterialVariantRepository extends JpaRepository<MaterialVariant, Long> {
    List<MaterialVariant> findByMaterialId(Integer materialId);
}
