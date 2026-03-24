package com.example.thisaraprinters.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;


@Entity
@Data
@Table(name = "customers")
public class CustomerModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String name;
    private String address;
    @NotNull
    private String email;
    @NotNull
    private String phone;
    private String contactperson;
    private String contactpersonphone;
    private LocalDate createddate;
    private LocalDate updateddate;

}
