package com.example.thisaraprinters.repository;

import com.example.thisaraprinters.model.StockLot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface StockLotRepository extends JpaRepository<StockLot, Long> {

    @Query("SELECT s FROM StockLot s WHERE s.variant.id = :variantId " +
           "AND s.widthMm >= :jobWidth AND s.heightMm >= :jobHeight " +
           "AND s.status = :status " +
           "ORDER BY CASE WHEN s.lotType = 'REMNANT' THEN 0 ELSE 1 END, " +
           "(s.widthMm * s.heightMm)")
    List<StockLot> findSuitableSheets(@Param("variantId") Long variantId,
                                      @Param("jobWidth") BigDecimal jobWidth,
                                      @Param("jobHeight") BigDecimal jobHeight,
                                      @Param("status") String status);

    List<StockLot> findByVariantId(Long variantId);

    List<StockLot> findByInventoryId(Integer inventoryId);
}
