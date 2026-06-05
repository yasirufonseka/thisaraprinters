package com.example.thisaraprinters.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "purchase_orders")
@Data
public class PurchaseOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @ManyToOne
    @JoinColumn(name = "price_request_id")
    private PriceRequest priceRequest;

    @Column(name = "order_date")
    private LocalDate orderDate;

    @Column(name = "items")
    private String items;

    @Column(name = "quantity")
    private String quantity;

    @Column(name = "payment_status")
    private String paymentStatus;

    @Column(name = "notes")
    private String notes;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "paid_amount")
    private Double paidAmount;

    @Column(name = "payment_proof")
    private String paymentProof; // Stores the file path or file name

    @Column(name = "created_date")
    private LocalDate createdDate;
}
