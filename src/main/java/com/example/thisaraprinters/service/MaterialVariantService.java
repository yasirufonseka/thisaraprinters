package com.example.thisaraprinters.service;

import com.example.thisaraprinters.model.MaterialVariant;
import com.example.thisaraprinters.repository.MaterialVariantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MaterialVariantService {

    private final MaterialVariantRepository variantRepository;

    @Autowired
    public MaterialVariantService(MaterialVariantRepository variantRepository) {
        this.variantRepository = variantRepository;
    }

    public List<MaterialVariant> getVariantsByMaterialId(Integer materialId) {
        return variantRepository.findByMaterialId(materialId);
    }

    public Optional<MaterialVariant> getVariantById(Long variantId) {
        return variantRepository.findById(variantId);
    }

    public MaterialVariant createVariant(MaterialVariant variant) {
        return variantRepository.save(variant);
    }

    public MaterialVariant updateVariant(Long variantId, MaterialVariant variant) {
        MaterialVariant existing = variantRepository.findById(variantId)
            .orElseThrow(() -> new RuntimeException("Variant not found"));

        if (variant.getGsm() != null) {
            existing.setGsm(variant.getGsm());
        }
        if (variant.getWidthMm() != null) {
            existing.setWidthMm(variant.getWidthMm());
        }
        if (variant.getHeightMm() != null) {
            existing.setHeightMm(variant.getHeightMm());
        }
        if (variant.getSheetsPerReam() != null) {
            existing.setSheetsPerReam(variant.getSheetsPerReam());
        }
        if (variant.getWeightPerUnitKg() != null) {
            existing.setWeightPerUnitKg(variant.getWeightPerUnitKg());
        }
        if (variant.getUnit() != null) {
            existing.setUnit(variant.getUnit());
        }
        if (variant.getReorderlevel() != null) {
            existing.setReorderlevel(variant.getReorderlevel());
        }
        if (variant.getStatus() != null) {
            existing.setStatus(variant.getStatus());
        }

        return variantRepository.save(existing);
    }

    public void deleteVariant(Long variantId) {
        variantRepository.deleteById(variantId);
    }
}
