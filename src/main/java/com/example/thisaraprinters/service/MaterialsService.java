package com.example.thisaraprinters.service;

import com.example.thisaraprinters.model.Materials;
import com.example.thisaraprinters.model.MaterialVariant;
import com.example.thisaraprinters.model.StockLot;
import com.example.thisaraprinters.repository.MaterialRepo;
import com.example.thisaraprinters.repository.MaterialVariantRepository;
import com.example.thisaraprinters.repository.StockLotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.thisaraprinters.model.JobMaterialUsage;
import com.example.thisaraprinters.model.ProductionModel;
import com.example.thisaraprinters.repository.JobMaterialUsageRepository;
import com.example.thisaraprinters.repository.ProductionRepo;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class MaterialsService {

    private final MaterialRepo materialRepo;
    private final MaterialVariantRepository variantRepo;
    private final StockLotRepository stockLotRepository;
    private final JobMaterialUsageRepository jobMaterialUsageRepository;
    private final ProductionRepo productionRepo;

    @Autowired
    public MaterialsService(MaterialRepo materialRepo,
                           MaterialVariantRepository variantRepo,
                           StockLotRepository stockLotRepository,
                           JobMaterialUsageRepository jobMaterialUsageRepository,
                           ProductionRepo productionRepo) {
        this.materialRepo = materialRepo;
        this.variantRepo = variantRepo;
        this.stockLotRepository = stockLotRepository;
        this.jobMaterialUsageRepository = jobMaterialUsageRepository;
        this.productionRepo = productionRepo;
    }

    public List<Materials> getAllMaterials() {
        List<Materials> list = materialRepo.findAll();
        for (Materials m : list) {
            populateTransientFields(m);
        }
        return list;
    }

    public Optional<Materials> getMaterialById(Integer id) {
        Optional<Materials> material = materialRepo.findById(id);
        material.ifPresent(this::populateTransientFields);
        return material;
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
        List<Materials> allMaterials = getAllMaterials();
        return allMaterials.stream()
            .filter(m -> m.getName().toLowerCase().contains(name.toLowerCase()))
            .toList();
    }

    public void populateTransientFields(Materials material) {
        List<MaterialVariant> variants = variantRepo.findByMaterialId(material.getId());
        int totalAvailable = 0;
        boolean isLowStock = false;
        boolean hasVariants = !variants.isEmpty();

        for (MaterialVariant variant : variants) {
            int variantAvailable = 0;
            List<StockLot> stockLots = stockLotRepository.findByVariantId(variant.getId());
            for (StockLot lot : stockLots) {
                if ("AVAILABLE".equals(lot.getStatus())) {
                    variantAvailable += lot.getQuantity().intValue();
                }
            }
            variant.setAvailableQuantity(variantAvailable);
            if (variantAvailable == 0) {
                variant.setCalculatedStatus("Out of Stock");
            } else if (variantAvailable <= variant.getReorderlevel()) {
                variant.setCalculatedStatus("Low Stock");
            } else {
                variant.setCalculatedStatus("Sufficient");
            }

            totalAvailable += variantAvailable;
            if (variantAvailable <= variant.getReorderlevel()) {
                isLowStock = true;
            }
        }

        material.setAvailableQuantity(totalAvailable);
        if (!hasVariants || totalAvailable == 0) {
            material.setCalculatedStatus("Out of Stock");
        } else if (isLowStock) {
            material.setCalculatedStatus("Low Stock");
        } else {
            material.setCalculatedStatus("Sufficient");
        }

        material.setVariants(variants);
    }

    private ProductionModel findJobByPurpose(String purpose) {
        if (purpose == null || purpose.trim().isEmpty()) {
            return null;
        }
        String cleanPurpose = purpose.trim();
        Optional<ProductionModel> jobOpt = productionRepo.findByOrderId(cleanPurpose);
        if (jobOpt.isPresent()) {
            return jobOpt.get();
        }

        try {
            int id = Integer.parseInt(cleanPurpose);
            jobOpt = productionRepo.findById(id);
            if (jobOpt.isPresent()) {
                return jobOpt.get();
            }
        } catch (NumberFormatException ignored) {}

        String numericOnly = cleanPurpose.replaceAll("[^0-9]", "");
        if (!numericOnly.isEmpty()) {
            try {
                int id = Integer.parseInt(numericOnly);
                jobOpt = productionRepo.findById(id);
                if (jobOpt.isPresent()) {
                    return jobOpt.get();
                }
            } catch (NumberFormatException ignored) {}
        }

        List<ProductionModel> allJobs = productionRepo.findAll();
        if (!allJobs.isEmpty()) {
            return allJobs.get(0);
        }

        return null;
    }

    @Transactional
    public String recordMaterialUsage(Integer materialId, Integer quantityUsed, String purpose) {
        try {
            if (quantityUsed == null || quantityUsed <= 0) {
                return "Quantity used must be greater than 0";
            }

            Optional<Materials> materialOpt = materialRepo.findById(materialId);
            if (materialOpt.isEmpty()) {
                return "Material not found";
            }

            int totalAvailable = getTotalAvailableQuantity(materialId);
            if (totalAvailable < quantityUsed) {
                return "Insufficient quantity. Available: " + totalAvailable + ", Required: " + quantityUsed;
            }

            ProductionModel job = findJobByPurpose(purpose);
            if (job == null) {
                return "Error: No production job found to associate with usage";
            }

            List<MaterialVariant> variants = variantRepo.findByMaterialId(materialId);
            BigDecimal remainingToConsume = new BigDecimal(quantityUsed);

            for (MaterialVariant variant : variants) {
                if (remainingToConsume.compareTo(BigDecimal.ZERO) <= 0) {
                    break;
                }

                List<StockLot> stockLots = stockLotRepository.findByVariantId(variant.getId());
                stockLots.sort((l1, l2) -> {
                    if ("REMNANT".equals(l1.getLotType()) && !"REMNANT".equals(l2.getLotType())) {
                        return -1;
                    }
                    if (!"REMNANT".equals(l1.getLotType()) && "REMNANT".equals(l2.getLotType())) {
                        return 1;
                    }
                    return l1.getCreatedAt().compareTo(l2.getCreatedAt());
                });

                for (StockLot lot : stockLots) {
                    if (remainingToConsume.compareTo(BigDecimal.ZERO) <= 0) {
                        break;
                    }

                    if (!"AVAILABLE".equals(lot.getStatus())) {
                        continue;
                    }

                    BigDecimal lotQty = lot.getQuantity();
                    if (lotQty.compareTo(BigDecimal.ZERO) <= 0) {
                        continue;
                    }

                    BigDecimal consumedQty;
                    if (lotQty.compareTo(remainingToConsume) > 0) {
                        consumedQty = remainingToConsume;
                        lot.setQuantity(lotQty.subtract(remainingToConsume));
                        stockLotRepository.save(lot);
                        remainingToConsume = BigDecimal.ZERO;
                    } else {
                        consumedQty = lotQty;
                        remainingToConsume = remainingToConsume.subtract(lotQty);
                        lot.setQuantity(BigDecimal.ZERO);
                        lot.setStatus("CONSUMED");
                        stockLotRepository.save(lot);
                    }

                    JobMaterialUsage usage = new JobMaterialUsage();
                    usage.setJobId((long) job.getId());
                    usage.setStockLot(lot);
                    usage.setQuantityUsed(consumedQty);
                    usage.setQuantityReturned(BigDecimal.ZERO);
                    jobMaterialUsageRepository.save(usage);
                }
            }

            return "Material usage recorded successfully";
        } catch (Exception e) {
            return "Error recording material usage: " + e.getMessage();
        }
    }
}
