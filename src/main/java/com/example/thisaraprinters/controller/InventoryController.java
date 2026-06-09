package com.example.thisaraprinters.controller;

import com.example.thisaraprinters.dto.InventoryDto;
import com.example.thisaraprinters.model.Inventory;
import com.example.thisaraprinters.model.Materials;
import com.example.thisaraprinters.model.MaterialVariant;
import com.example.thisaraprinters.service.InventoryService;
import com.example.thisaraprinters.service.MaterialsService;
import com.example.thisaraprinters.service.MaterialVariantService;
import com.example.thisaraprinters.service.SupplierService;
import com.example.thisaraprinters.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/inventory")
public class InventoryController {

    private final MaterialsService materialsService;
    private final InventoryService inventoryService;
    private final MaterialVariantService variantService;
    private final SupplierService supplierService;
    private final UserService userService;

    @Autowired
    public InventoryController(MaterialsService materialsService,
                              InventoryService inventoryService,
                              MaterialVariantService variantService,
                              SupplierService supplierService,
                              UserService userService) {
        this.materialsService = materialsService;
        this.inventoryService = inventoryService;
        this.variantService = variantService;
        this.supplierService = supplierService;
        this.userService = userService;
    }

    @GetMapping("/management")
    public ModelAndView showInventoryManagement() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("inventoryManagement");
        mav.addObject("materials", materialsService.getAllMaterials());
        mav.addObject("suppliers", supplierService.getAllSuppliers());
        mav.addObject("users", userService.getAllUsers());
        mav.addObject("grns", inventoryService.getAllGRNs());
        return mav;
    }

    @GetMapping("/api/materials")
    @ResponseBody
    public ResponseEntity<List<Materials>> getAllMaterials() {
        List<Materials> materials = materialsService.getAllMaterials();
        return ResponseEntity.ok(materials);
    }

    @GetMapping("/api/materials/{id}")
    @ResponseBody
    public ResponseEntity<?> getMaterialById(@PathVariable Integer id) {
        Optional<Materials> material = materialsService.getMaterialById(id);
        if (material.isPresent()) {
            return ResponseEntity.ok(material.get());
        }
        return ResponseEntity.status(404).body(Map.of("message", "Material not found"));
    }

    @GetMapping("/api/materials/{materialId}/variants")
    @ResponseBody
    public ResponseEntity<List<MaterialVariant>> getMaterialVariants(@PathVariable Integer materialId) {
        List<MaterialVariant> variants = variantService.getVariantsByMaterialId(materialId);
        return ResponseEntity.ok(variants);
    }

    @PostMapping("/api/materials/add")
    @ResponseBody
    public ResponseEntity<Map<String, String>> addMaterial(@RequestBody Materials material) {
        String result = materialsService.addMaterial(material);
        int statusCode = result.contains("successfully") ? 201 : 400;
        return ResponseEntity.status(statusCode).body(Map.of("message", result));
    }

    @PutMapping("/api/materials/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, String>> updateMaterial(@PathVariable Integer id, @RequestBody Materials material) {
        String result = materialsService.updateMaterial(id, material);
        int statusCode = result.contains("successfully") ? 200 : 400;
        return ResponseEntity.status(statusCode).body(Map.of("message", result));
    }

    @DeleteMapping("/api/materials/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, String>> deleteMaterial(@PathVariable Integer id) {
        String result = materialsService.deleteMaterial(id);
        int statusCode = result.contains("successfully") ? 200 : 404;
        return ResponseEntity.status(statusCode).body(Map.of("message", result));
    }

    @PostMapping("/api/grn/save-full")
    @ResponseBody
    public ResponseEntity<Map<String, String>> saveGRNFull(@RequestBody InventoryDto inventory) {
        String result = inventoryService.saveGRN(inventory);
        int statusCode = result.contains("successfully") ? 200 : 400;
        return ResponseEntity.status(statusCode).body(Map.of("message", result));
    }

    @GetMapping("/api/materials/search/{name}")
    @ResponseBody
    public ResponseEntity<List<Materials>> searchMaterials(@PathVariable String name) {
        List<Materials> materials = materialsService.searchMaterialsByName(name);
        return ResponseEntity.ok(materials);
    }
}
