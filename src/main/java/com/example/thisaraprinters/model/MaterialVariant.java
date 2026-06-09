package com.example.thisaraprinters.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "material_variants")
@Data
public class MaterialVariant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material_id", nullable = false)
    @JsonIgnore
    private Materials material;

    @Column(name = "gsm")
    private Integer gsm;

    @Column(name = "width_mm", precision = 8, scale = 2)
    private BigDecimal widthMm;

    @Column(name = "height_mm", precision = 8, scale = 2)
    private BigDecimal heightMm;

    @Column(name = "sheets_per_ream")
    private Integer sheetsPerReam;

    @Column(name = "weight_per_unit_kg", precision = 8, scale = 3)
    private BigDecimal weightPerUnitKg;

    @Column(name = "unit", nullable = false, length = 50)
    private String unit;

    @Column(name = "reorderlevel", nullable = false)
    private Integer reorderlevel;

    @Column(name = "status", nullable = false, length = 50)
    private String status;

    @OneToMany(mappedBy = "variant", fetch = FetchType.LAZY)
    private List<StockLot> stockLots = new ArrayList<>();

    @OneToMany(mappedBy = "variant", fetch = FetchType.LAZY)
    private List<Inventory> inventories = new ArrayList<>();
}
