package com.example.thisaraprinters.controller;

import com.example.thisaraprinters.model.ProductionModel;
import com.example.thisaraprinters.service.ProductionService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/production")
public class ProductionController {

    private final ProductionService productionService;

    public ProductionController(ProductionService productionService) {
        this.productionService = productionService;
    }

    @GetMapping("/management")
    public ModelAndView getProductionView() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("production");
        return mav;
    }

    @GetMapping("/all")
    @ResponseBody
    public List<ProductionModel> getAllProductionJobs() {
        return productionService.getAllJobs();
    }

    @GetMapping("/get/{id}")
    @ResponseBody
    public ProductionModel getProductionJob(@PathVariable("id") int id) {
        return productionService.getJobById(id);
    }

    @GetMapping("/get-by-order/{orderId}")
    @ResponseBody
    public ProductionModel getProductionJobByOrderId(@PathVariable("orderId") String orderId) {
        return productionService.getJobByOrderId(orderId);
    }

    @PostMapping("/update-status")
    @ResponseBody
    public ResponseEntity<Map<String, String>> updateStatus(@RequestBody Map<String, String> payload) {
        String orderId = payload.get("orderId");
        String status = payload.get("status");
        if (orderId == null || status == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "orderId and status are required"));
        }
        productionService.updateJobStatus(orderId, status);
        return ResponseEntity.ok(Map.of("message", "Status updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, String>> deleteJob(@PathVariable("id") int id) {
        productionService.deleteJob(id);
        return ResponseEntity.ok(Map.of("message", "Production job deleted successfully"));
    }

    @DeleteMapping("/delete-by-order/{orderId}")
    @ResponseBody
    public ResponseEntity<Map<String, String>> deleteJobByOrderId(@PathVariable("orderId") String orderId) {
        productionService.deleteJobByOrderId(orderId);
        return ResponseEntity.ok(Map.of("message", "Production job deleted successfully"));
    }
}
