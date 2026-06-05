package com.example.thisaraprinters.dto;

import com.example.thisaraprinters.model.Materials;
import com.example.thisaraprinters.model.Supplier;
import com.example.thisaraprinters.model.UserModel;
import lombok.Data;

import java.time.LocalDate;

@Data
public class InventoryDto {


    private String grnNumber;
    private String supplierInvoiceNo;
    private String batchNo;
    private Integer recivedquantity;
    private String units;
    private LocalDate expiryDate;
    private LocalDate receivedDate;
    private String notes;
    private Materials itemname;
    private Supplier suppliers;
    private UserModel receivedByUser;
}

