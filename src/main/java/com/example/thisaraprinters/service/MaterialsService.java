package com.example.thisaraprinters.service;

import com.example.thisaraprinters.model.Materials;
import com.example.thisaraprinters.repository.MaterialRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MaterialsService {

    private final MaterialRepo materialRepo;

    @Autowired
    public MaterialsService(MaterialRepo materialRepo) {
        this.materialRepo = materialRepo;
    }

    // Get all materials
    public List<Materials> getAllMaterials() {
        return materialRepo.findAll();
    }

    // Get material by ID
    public Optional<Materials> getMaterialById(Integer id) {
        return materialRepo.findById(id);
    }

    // Add new material
    public String addMaterial(Materials material) {
        try {
            // Validate required fields
            if (material.getMaterial() == null || material.getMaterial().isEmpty()) {
                return "Material name is required";
            }
            if (material.getAvailablequantity() == null) {
                return "Available quantity is required";
            }
            if (material.getUnits() == null || material.getUnits().isEmpty()) {
                return "Units are required";
            }
            if (material.getReorderlevel() == null) {
                return "Reorder level is required";
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

    // Update material
    public String updateMaterial(Integer id, Materials material) {
        try {
            Optional<Materials> existingMaterial = materialRepo.findById(id);
            
            if (existingMaterial.isEmpty()) {
                return "Material not found";
            }

            Materials materialToUpdate = existingMaterial.get();
            
            if (material.getMaterial() != null && !material.getMaterial().isEmpty()) {
                materialToUpdate.setMaterial(material.getMaterial());
            }
            if (material.getAvailablequantity() != null) {
                materialToUpdate.setAvailablequantity(material.getAvailablequantity());
            }
            if (material.getUnits() != null && !material.getUnits().isEmpty()) {
                materialToUpdate.setUnits(material.getUnits());
            }
            if (material.getReorderlevel() != null) {
                materialToUpdate.setReorderlevel(material.getReorderlevel());
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

    // Delete material
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

    // Record material usage
    public String recordMaterialUsage(Integer materialId, Integer quantityUsed) {
        try {
            if (quantityUsed == null || quantityUsed <= 0) {
                return "Quantity used must be greater than 0";
            }

            Optional<Materials> material = materialRepo.findById(materialId);
            
            if (material.isEmpty()) {
                return "Material not found";
            }

            Materials mat = material.get();
            Integer currentQuantity = mat.getAvailablequantity() == null ? 0 : mat.getAvailablequantity();
            
            if (currentQuantity < quantityUsed) {
                return "Insufficient quantity. Available: " + currentQuantity + ", Required: " + quantityUsed;
            }

            mat.setAvailablequantity(currentQuantity - quantityUsed);
            updateMaterialStatus(mat);

            materialRepo.save(mat);
            return "Material usage recorded successfully";
        } catch (Exception e) {
            return "Error recording material usage: " + e.getMessage();
        }
    }

    // Receive goods (GRN)
    public String receiveGoods(Integer materialId, Integer quantityReceived) {
        try {
            if (quantityReceived == null || quantityReceived <= 0) {
                return "Quantity received must be greater than 0";
            }

            Optional<Materials> material = materialRepo.findById(materialId);
            
            if (material.isEmpty()) {
                return "Material not found";
            }

            Materials mat = material.get();
            Integer currentQuantity = mat.getAvailablequantity() == null ? 0 : mat.getAvailablequantity();
            mat.setAvailablequantity(currentQuantity + quantityReceived);
            updateMaterialStatus(mat);

            materialRepo.save(mat);
            return "Goods received successfully";
        } catch (Exception e) {
            return "Error receiving goods: " + e.getMessage();
        }
    }

    // Search materials by name
    public List<Materials> searchMaterialsByName(String name) {
        List<Materials> allMaterials = materialRepo.findAll();
        return allMaterials.stream()
            .filter(m -> m.getMaterial().toLowerCase().contains(name.toLowerCase()))
            .toList();
    }

    private void updateMaterialStatus(Materials material) {
        int availableQuantity = material.getAvailablequantity() == null ? 0 : material.getAvailablequantity();
        int reorderLevel = material.getReorderlevel() == null ? 0 : material.getReorderlevel();

        if (availableQuantity == 0) {
            material.setStatus("Out of Stock");
        } else if (availableQuantity < reorderLevel) {
            material.setStatus("Low Stock");
        } else {
            material.setStatus("Sufficient");
        }
    }
}
