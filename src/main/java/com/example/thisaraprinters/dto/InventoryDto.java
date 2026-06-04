package com.example.thisaraprinters.dto;

import com.example.thisaraprinters.model.Materials;
import com.example.thisaraprinters.model.Supplier;
import com.example.thisaraprinters.model.UserModel;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class InventoryDto {

    private Integer id;
    private String grnNumber;
    private String supplierInvoiceNo;
    private String batchNo;
    private Integer recivedquantity;
    private String units;
    private LocalDate expiryDate;
    private String notes;
    private List<Materials> itemname;
    private Supplier suppliers;
    private UserModel receivedByUser;
}

