package com.example.thisaraprinters.service;

import com.example.thisaraprinters.model.Inventory;
import com.example.thisaraprinters.model.StockLot;
import com.example.thisaraprinters.repository.StockLotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class StockLotService {

    private final StockLotRepository stockLotRepository;

    @Autowired
    public StockLotService(StockLotRepository stockLotRepository) {
        this.stockLotRepository = stockLotRepository;
    }

    public StockLot createGrnLot(Inventory inventory) {
        StockLot stockLot = new StockLot();
        stockLot.setVariant(inventory.getVariant());
        stockLot.setInventory(inventory);
        stockLot.setLotType("GRN");
        stockLot.setQuantity(new BigDecimal(inventory.getReceivedquantity()));
        stockLot.setUnit(inventory.getVariant().getUnit());
        stockLot.setWidthMm(inventory.getVariant().getWidthMm());
        stockLot.setHeightMm(inventory.getVariant().getHeightMm());
        stockLot.setWeightKg(inventory.getVariant().getWeightPerUnitKg());
        stockLot.setStatus("AVAILABLE");
        stockLot.setSourceRef(inventory.getGrnNumber());
        stockLot.setCreatedAt(LocalDateTime.now());

        return stockLotRepository.save(stockLot);
    }

    public StockLot createRemnantLot(Long jobId, Long originalLotId, BigDecimal returnedQty,
                                    BigDecimal actualWidthMm, BigDecimal actualHeightMm) {
        StockLot originalLot = stockLotRepository.findById(originalLotId)
            .orElseThrow(() -> new RuntimeException("Original stock lot not found"));

        StockLot remnantLot = new StockLot();
        remnantLot.setVariant(originalLot.getVariant());
        remnantLot.setInventory(null);
        remnantLot.setLotType("REMNANT");
        remnantLot.setQuantity(returnedQty);
        remnantLot.setUnit(originalLot.getUnit());
        remnantLot.setWidthMm(actualWidthMm);
        remnantLot.setHeightMm(actualHeightMm);
        remnantLot.setWeightKg(originalLot.getWeightKg());
        remnantLot.setStatus("AVAILABLE");
        remnantLot.setSourceRef("JOB-" + jobId);
        remnantLot.setCreatedAt(LocalDateTime.now());

        return stockLotRepository.save(remnantLot);
    }
}
