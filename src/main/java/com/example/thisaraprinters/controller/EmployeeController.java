package com.example.thisaraprinters.controller;

import com.example.thisaraprinters.dto.EmployeeDto;
import com.example.thisaraprinters.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    // automaticaly create a instence of employee service
    @Autowired
    private EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;

    }

    @GetMapping("/getemployees")
    public ModelAndView showEmployees() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("employee");
        return mav;
    }

    @PostMapping("/add/employee")
    public ResponseEntity<?> addEmployee(@RequestBody EmployeeDto employee) {
        try {
            System.out.println(employee.getFullname());
            employeeService.saveEmployee(employee);
            return ResponseEntity.ok(Map.of("message", "successfully added the employee"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error adding employee"));
        }
    }

    @GetMapping("/get/alldata")
    public ResponseEntity<?> getAllEmployees() {
        try {
            List employees = employeeService.getAllEmployees();
            return ResponseEntity.ok(employees);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error getting employees"));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable("id") Integer id, @RequestBody EmployeeDto employee) {
        try {
            employeeService.updateEmployee(id, employee);
            return ResponseEntity.ok(Map.of("message", "successfully updated the employee"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error updating employee"));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable("id") Integer id) {
        try {
            employeeService.deleteEmployee(id);
            return ResponseEntity.ok(Map.of("message", "successfully deleted the employee"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error deleting employee"));
        }
    }

}