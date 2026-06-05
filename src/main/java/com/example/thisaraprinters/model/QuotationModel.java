package com.example.thisaraprinters.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Entity
@Data
@Table(name = "quotations")
public class QuotationModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String productsize;
    private Integer quantity;
    private String color;
    private String quotationdescription;
    private String cuttingtype;
    private String foiling;
    private String lamination;
    private String bindingtype;
    private double quotationamount;
    private double advanceamount;
    private LocalDate quotationdate;
    private String quotationstatus;
   // private Integer quotationvalidity;
    private LocalDate expiryDate;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customersid")
    private CustomerModel customer;
    @ManyToMany()
    @JoinTable(
            name = "_quotations_has_materials",
            joinColumns = @JoinColumn(name = "quotations_id"),
            inverseJoinColumns = @JoinColumn(name = "material_id")

    )
    private List<Materials> materialsList;
}
