package com.example.thisaraprinters.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Table(name = "customers")
public class CustomerModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotNull
    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @NotNull
    @Column(name = "email")
    private String email;

    @NotNull
    @Column(name = "phone")
    private String phone;

    @Column(name = "contactperson")
    private String contactperson;

    @Column(name = "contactpersonphone")
    private String contactpersonphone;

    @Column(name = "createddate")
    private LocalDate createddate;

    @Column(name = "updateddate")
    private LocalDate updateddate;

    @OneToMany(mappedBy = "customer")
    private List<QuotationModel> quotations;

}
