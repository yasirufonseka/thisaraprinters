package com.example.thisaraprinters.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;

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
    
    @Column(name="recivedquantity")
    private Integer recivedquantity;
    
    @Column(name="units")
    private String units;
    
    @Column(name = "expirydate")
    private LocalDate expiryDate;

    @Column(name = "receiveddate")
    private LocalDate receivedDate;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material_id")
    private Materials itemname;
    
    @JoinColumn(name = "supplier_id",referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Supplier suppliers;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "received_by_user_id")
    private UserModel receivedByUser;
}

