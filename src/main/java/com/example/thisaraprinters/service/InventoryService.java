package com.example.thisaraprinters.service;

import com.example.thisaraprinters.dto.InventoryDto;
import com.example.thisaraprinters.model.Inventory;
import com.example.thisaraprinters.model.MaterialVariant;
import com.example.thisaraprinters.model.Supplier;
import com.example.thisaraprinters.model.UserModel;
import com.example.thisaraprinters.repository.InventoryRepository;
import com.example.thisaraprinters.repository.MaterialVariantRepository;
import com.example.thisaraprinters.repository.SupplierRepo;
import com.example.thisaraprinters.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Sort;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final MaterialVariantRepository variantRepo;
    private final SupplierRepo supplierRepo;
    private final UserRepo userRepo;
    private final StockLotService stockLotService;

    @Autowired
    public InventoryService(InventoryRepository inventoryRepository,
                            MaterialVariantRepository variantRepo,
                            SupplierRepo supplierRepo,
                            UserRepo userRepo,
                            StockLotService stockLotService) {
        this.inventoryRepository = inventoryRepository;
        this.variantRepo = variantRepo;
        this.supplierRepo = supplierRepo;
        this.userRepo = userRepo;
        this.stockLotService = stockLotService;
    }

    @Transactional
    public String saveGRN(InventoryDto inventory) {
        try {
            String validationError = validateGRN(inventory);
            if (validationError != null) {
                return validationError;
            }

            MaterialVariant variant = variantRepo.findById(Long.valueOf(inventory.getVariantId())).orElse(null);
            if (variant == null) {
                return "Error: Material variant not found";
            }

            Supplier supplier = supplierRepo.findById(inventory.getSuppliers().getId()).orElse(null);
            if (supplier == null) {
                return "Error: Supplier not found";
            }

            UserModel receivedBy = userRepo.findById(inventory.getReceivedByUser().getId()).orElse(null);
            if (receivedBy == null) {
                return "Error: Received By user not found";
            }

            if (!variant.getUnit().equalsIgnoreCase(inventory.getUnits().trim())) {
                return "Error: GRN unit must match variant unit: " + variant.getUnit();
            }

            LocalDate receivedDate = inventory.getReceivedDate() != null ? inventory.getReceivedDate() : LocalDate.now();
            if (inventory.getExpiryDate() != null && inventory.getExpiryDate().isBefore(receivedDate)) {
                return "Error: Expiry Date cannot be before Received Date";
            }

            Inventory inventoryEntity = new Inventory();
            inventoryEntity.setGrnNumber(generateNextGRNNumber());
            inventoryEntity.setSupplierInvoiceNo(inventory.getSupplierInvoiceNo().trim());
            inventoryEntity.setBatchNo(inventory.getBatchNo().trim());
            inventoryEntity.setReceivedquantity(inventory.getReceivedquantity());
            inventoryEntity.setExpiryDate(inventory.getExpiryDate());
            inventoryEntity.setReceivedDate(receivedDate);
            inventoryEntity.setNotes(inventory.getNotes());
            inventoryEntity.setVariant(variant);
            inventoryEntity.setSuppliers(supplier);
            inventoryEntity.setReceivedByUser(receivedBy);

            inventoryRepository.save(inventoryEntity);

            stockLotService.createGrnLot(inventoryEntity);

            return "GRN saved successfully: " + inventoryEntity.getGrnNumber();
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return "Error: " + e.getMessage();
        }
    }

    public synchronized String generateNextGRNNumber() {
        String currentYear = String.valueOf(LocalDate.now().getYear());
        String yearPattern = "GRN-" + currentYear + "-%";

        String lastGrn = inventoryRepository.findLastGrnNumberForYear(yearPattern);
        int nextNumber = 1;

        if (lastGrn != null) {
            String[] parts = lastGrn.split("-");
            try {
                if (parts.length == 3) {
                    nextNumber = Integer.parseInt(parts[2].trim()) + 1;
                }
            } catch (NumberFormatException e) {
                nextNumber = 1;
            }
        }

        return String.format("GRN-%s-%04d", currentYear, nextNumber);
    }

    private String validateGRN(InventoryDto inventory) {
        if (inventory == null) {
            return "Error: GRN data is required";
        }
        if (inventory.getSupplierInvoiceNo() == null || inventory.getSupplierInvoiceNo().trim().isEmpty()) {
            return "Error: Supplier Invoice Number is required";
        }
        if (inventory.getBatchNo() == null || inventory.getBatchNo().trim().isEmpty()) {
            return "Error: Batch Number is required";
        }
        if (inventory.getReceivedquantity() == null || inventory.getReceivedquantity() <= 0) {
            return "Error: Received Quantity must be greater than 0";
        }
        if (inventory.getUnits() == null || inventory.getUnits().trim().isEmpty()) {
            return "Error: Units is required";
        }
        if (inventory.getSuppliers() == null || inventory.getSuppliers().getId() == null) {
            return "Error: Supplier is required";
        }
        if (inventory.getVariantId() == null) {
            return "Error: Material variant is required";
        }
        if (inventory.getReceivedByUser() == null || inventory.getReceivedByUser().getId() == null) {
            return "Error: Received By user is required";
        }

        return null;
    }

    public List<Inventory> getAllGRNs() {
        return inventoryRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }
}
