package com.example.thisaraprinters.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Date;
import lombok.Data;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;

@Entity
@Data
@Table(name = "quotations")
public class QuotationModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
private Long customersid;
private String papertype;
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
private Integer quotationvalidity;
private LocalDate orderplaceddate;
private LocalDate addeddate;


}
