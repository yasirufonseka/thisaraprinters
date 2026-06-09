package com.example.thisaraprinters.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name="inventory")
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "grnnumber")
    private String grnNumber;

    @Column(name = "supplierinvoiceno")
    private String supplierInvoiceNo;

    @Column(name = "batchno")
    private String batchNo;

    @Column(name="receivedquantity")
    private Integer receivedquantity;

    @Column(name = "expirydate")
    private LocalDate expiryDate;

    @Column(name = "receiveddate")
    private LocalDate receivedDate;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_id")
    @JsonIgnore
    private MaterialVariant variant;

    @JoinColumn(name = "supplier_id",referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Supplier suppliers;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "received_by_user_id")
    @JsonIgnore
    private UserModel receivedByUser;

    @ToString.Exclude
    @OneToMany(mappedBy = "inventory", fetch = FetchType.LAZY)
    private List<StockLot> stockLots = new ArrayList<>();
}


