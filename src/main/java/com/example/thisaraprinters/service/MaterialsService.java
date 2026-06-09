package com.example.thisaraprinters.service;

import com.example.thisaraprinters.model.Materials;
import com.example.thisaraprinters.model.MaterialVariant;
import com.example.thisaraprinters.model.StockLot;
import com.example.thisaraprinters.repository.MaterialRepo;
import com.example.thisaraprinters.repository.MaterialVariantRepository;
import com.example.thisaraprinters.repository.StockLotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class MaterialsService {

    private final MaterialRepo materialRepo;
    private final MaterialVariantRepository variantRepo;
    private final StockLotRepository stockLotRepository;

    @Autowired
    public MaterialsService(MaterialRepo materialRepo,
                           MaterialVariantRepository variantRepo,
                           StockLotRepository stockLotRepository) {
        this.materialRepo = materialRepo;
        this.variantRepo = variantRepo;
        this.stockLotRepository = stockLotRepository;
    }

    public List<Materials> getAllMaterials() {
        return materialRepo.findAll();
    }

    public Optional<Materials> getMaterialById(Integer id) {
        return materialRepo.findById(id);
    }

    public String addMaterial(Materials material) {
        try {
            if (material.getName() == null || material.getName().isEmpty()) {
                return "Material name is required";
            }
            if (material.getStatus() == null || material.getStatus().isEmpty()) {
                return "Status is required";
            }

            materialRepo.save(material);
            return "Material added successfully";
        } catch (Exception e) {
            return "Error adding material: " + e.getMessage();
        }
    }

    public String updateMaterial(Integer id, Materials material) {
        try {
            Optional<Materials> existingMaterial = materialRepo.findById(id);

            if (existingMaterial.isEmpty()) {
                return "Material not found";
            }

            Materials materialToUpdate = existingMaterial.get();

            if (material.getName() != null && !material.getName().isEmpty()) {
                materialToUpdate.setName(material.getName());
            }
            if (material.getStatus() != null && !material.getStatus().isEmpty()) {
                materialToUpdate.setStatus(material.getStatus());
            }

            materialRepo.save(materialToUpdate);
            return "Material updated successfully";
        } catch (Exception e) {
            return "Error updating material: " + e.getMessage();
        }
    }

    public String deleteMaterial(Integer id) {
        try {
            Optional<Materials> material = materialRepo.findById(id);

            if (material.isEmpty()) {
                return "Material not found";
            }

            materialRepo.deleteById(id);
            return "Material deleted successfully";
        } catch (Exception e) {
            return "Error deleting material: " + e.getMessage();
        }
    }

    public Integer getTotalAvailableQuantity(Integer materialId) {
        List<MaterialVariant> variants = variantRepo.findByMaterialId(materialId);
        int totalQuantity = 0;

        for (MaterialVariant variant : variants) {
            List<StockLot> stockLots = stockLotRepository.findByVariantId(variant.getId());
            for (StockLot lot : stockLots) {
                if ("AVAILABLE".equals(lot.getStatus())) {
                    totalQuantity += lot.getQuantity().intValue();
                }
            }
        }

        return totalQuantity;
    }

    public List<Materials> searchMaterialsByName(String name) {
        List<Materials> allMaterials = materialRepo.findAll();
        return allMaterials.stream()
            .filter(m -> m.getName().toLowerCase().contains(name.toLowerCase()))
            .toList();
    }
}
