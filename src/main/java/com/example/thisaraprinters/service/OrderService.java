package com.example.thisaraprinters.service;

import com.example.thisaraprinters.dto.QuotationDto;
import com.example.thisaraprinters.model.CustomerModel;
import com.example.thisaraprinters.model.Materials;
import com.example.thisaraprinters.model.QuotationModel;
import com.example.thisaraprinters.repository.CustomerRepo;
import com.example.thisaraprinters.repository.MaterialRepo;
import com.example.thisaraprinters.repository.QuotationRepo;
import org.jspecify.annotations.Nullable;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class OrderService {

    private CustomerRepo customerRepo;
    private MaterialRepo materialsRepo;
    private QuotationRepo quotationRepo;

    public OrderService(MaterialRepo materialsRepo, CustomerRepo customerRepo,QuotationRepo quotationRepo) {
        this.customerRepo = customerRepo;
        this.materialsRepo = materialsRepo;
        this.quotationRepo = quotationRepo;
    }

    public String saveQuotation(QuotationDto quotation) {

        if (quotation.getMaterialsList() == null || quotation.getMaterialsList().isEmpty()) {
            throw new ArrayStoreException("Materials should not be empty");

        }
            //create object ow quotation table
            QuotationModel newQuotation = new QuotationModel();

            //set valus to the object from frontend
            newQuotation.setProductsize(quotation.getProductsize().toUpperCase());
            newQuotation.setQuantity(quotation.getQuantity());
            newQuotation.setColor(quotation.getColor());
            newQuotation.setCustomer(quotation.getCustomer());
            newQuotation.setQuotationdescription(quotation.getQuotationdescription().toUpperCase());
            newQuotation.setCuttingtype(quotation.getCuttingtype().toUpperCase());
            newQuotation.setFoiling(quotation.getFoiling());
            newQuotation.setLamination(quotation.getLamination());
            newQuotation.setBindingtype(quotation.getBindingtype().toUpperCase());
            newQuotation.setQuotationamount(quotation.getQuotationamount());
            newQuotation.setAdvanceamount(quotation.getAdvanceamount());
            newQuotation.setQuotationstatus(quotation.getQuotationstatus().toUpperCase());
            newQuotation.setQuotationdate(LocalDate.now());
            newQuotation.setExpiryDate(calculateExpiryDate());
            newQuotation.setMaterialsList(quotation.getMaterialsList());

            quotationRepo.save(newQuotation);

            return "Quatation saved successfully";
//        } catch (Exception e) {
//            throw new RuntimeException("Error while saving quotation"+ e.getMessage())  ;
//        }

    }

    private LocalDate calculateExpiryDate(){
        LocalDate createdDate = LocalDate.now();
        LocalDate expiryDate = createdDate.plusDays(7);
        return expiryDate;

    }


    public List<CustomerModel> getAllCustomers() {
        System.out.println(customerRepo.findAll());
       return customerRepo.findAll();
    }

    public  List<Materials> getAllMaterials() {
        return materialsRepo.findAll();
    }
}