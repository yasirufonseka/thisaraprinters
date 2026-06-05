package com.example.thisaraprinters.controller;

import com.example.thisaraprinters.dto.QuotationDto;
import com.example.thisaraprinters.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;


    public  OrderController(OrderService orderService){
        this.orderService = orderService;
    }
    @GetMapping("/management")
    public ModelAndView getOrderView() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("ordermanagement");
        mav.addObject("customerList",orderService.getAllCustomers());
        mav.addObject("materialList",orderService.getAllMaterials());
        return mav;
    }

    @PostMapping("/save/quotation")
    public ResponseEntity<?> saveQuotation(@RequestBody QuotationDto quotation) {
       return ResponseEntity.status(200).body(Map.of("message", orderService.saveQuotation(quotation)));


    }

}
