package com.example.thisaraprinters.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name="quotations")
public class QuotationModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double productsize;
    private int quantity;
    private String color;
    private String papertype;
    private String bindingtype;
    private String cuttingtype;
    private String foiling;
    private String lamination;
    private Date orderplaceddate;
    private String quotationdate;
    private String quotationstatus;
    private String advanceamount;
    private double quotationamount;
    private String quotationvalidity;
    private Date addeddate;
    private String quotationdescription;
    @OneToOne
    @JoinColumn(name = "id")
    private CustomerModel customerid;
    @OneToOne
    @JoinColumn(name = "id")
    private OrderModel orderid;

}
