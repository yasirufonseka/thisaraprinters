package com.example.thisaraprinters.service;

import com.example.thisaraprinters.model.ProductionModel;
import com.example.thisaraprinters.repository.ProductionRepo;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ProductionService {

    private final ProductionRepo productionRepo;

    public ProductionService(ProductionRepo productionRepo) {
        this.productionRepo = productionRepo;
    }

    @PostConstruct
    public void seedMockData() {
        if (productionRepo.count() == 0) {
            ProductionModel j1 = new ProductionModel();
            j1.setOrderId("ORD-1025");
            j1.setCustomerName("Saman Kumara");
            j1.setDescription("Business Cards (1000pcs)");
            j1.setDeadline(LocalDate.of(2026, 2, 20));
            j1.setPriority("Urgent");
            j1.setStatus("New Orders");
            productionRepo.save(j1);

            ProductionModel j2 = new ProductionModel();
            j2.setOrderId("ORD-1015");
            j2.setCustomerName("Nimal Perera");
            j2.setDescription("Wedding Invitations");
            j2.setDeadline(LocalDate.of(2026, 2, 26));
            j2.setPriority("Normal");
            j2.setStatus("Printing");
            productionRepo.save(j2);

            ProductionModel j3 = new ProductionModel();
            j3.setOrderId("ORD-1030");
            j3.setCustomerName("Tech Solutions");
            j3.setDescription("Annual Report");
            j3.setDeadline(LocalDate.of(2026, 2, 22));
            j3.setPriority("High");
            j3.setStatus("Design Phase");
            productionRepo.save(j3);

            ProductionModel j4 = new ProductionModel();
            j4.setOrderId("ORD-1010");
            j4.setCustomerName("School Book Project");
            j4.setDescription("Textbooks (Grade 5)");
            j4.setDeadline(LocalDate.of(2026, 2, 28));
            j4.setPriority("Normal");
            j4.setStatus("Finishing");
            productionRepo.save(j4);

            ProductionModel j5 = new ProductionModel();
            j5.setOrderId("ORD-1005");
            j5.setCustomerName("Local Council");
            j5.setDescription("Posters (A3)");
            j5.setDeadline(LocalDate.of(2026, 2, 15));
            j5.setPriority("Normal");
            j5.setStatus("Ready to Deliver");
            productionRepo.save(j5);

            System.out.println("================= Production mock records seeded successfully =================");
        }
    }

    public List<ProductionModel> getAllJobs() {
        return productionRepo.findAll();
    }

    public ProductionModel getJobById(int id) {
        return productionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Production job not found for ID: " + id));
    }

    public ProductionModel getJobByOrderId(String orderId) {
        return productionRepo.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Production job not found for Order ID: " + orderId));
    }

    public void updateJobStatus(String orderId, String newStatus) {
        ProductionModel job = getJobByOrderId(orderId);
        job.setStatus(newStatus);
        productionRepo.save(job);
    }

    public void deleteJob(int id) {
        if (!productionRepo.existsById(id)) {
            throw new RuntimeException("Production job not found for ID: " + id);
        }
        productionRepo.deleteById(id);
    }

    public void deleteJobByOrderId(String orderId) {
        ProductionModel job = getJobByOrderId(orderId);
        productionRepo.delete(job);
    }
}
