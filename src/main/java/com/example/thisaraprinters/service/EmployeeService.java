package com.example.thisaraprinters.service;

import com.example.thisaraprinters.dto.EmployeeDto;
import com.example.thisaraprinters.model.EmployeeModel;
import com.example.thisaraprinters.repository.EmployeeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class EmployeeService {
    @Autowired // create an instence of the repository
    private final EmployeeRepo employeeRepo;

    public EmployeeService(EmployeeRepo employeeRepo) {
        this.employeeRepo = employeeRepo;
    }

    public String saveEmployee(EmployeeDto employee) {

        if (employee == null) {
            return "Employee is null";
        } else {

            EmployeeModel newEmployee = new EmployeeModel();
            newEmployee.setFullname(employee.getFullName());
            newEmployee.setCallingname(employee.getCallingName());
            newEmployee.setNic(employee.getNic());
            newEmployee.setDob(employee.getDob());
            newEmployee.setGender(employee.getGender());
            newEmployee.setEmail(employee.getEmail());
            newEmployee.setPhonenumber(employee.getPhoneNo());
            newEmployee.setAddress(employee.getAddress());
            newEmployee.setPosition(employee.getPosition());
            newEmployee.setAddedDate(new Date());

            employeeRepo.save(newEmployee);

            return "Adding employee is success!";
        }

    }

    public List<EmployeeModel> getAllEmployees() {

        return employeeRepo.findAll();
    }

    public String updateEmployee(Long id, EmployeeDto employee) {

        EmployeeModel existingEmployee = employeeRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));

        existingEmployee.setFullname(employee.getFullName());
        existingEmployee.setCallingname(employee.getCallingName());
        existingEmployee.setNic(employee.getNic());
        existingEmployee.setDob(employee.getDob());
        existingEmployee.setGender(employee.getGender());
        existingEmployee.setEmail(employee.getEmail());
        existingEmployee.setPhonenumber(employee.getPhoneNo());
        existingEmployee.setAddress(employee.getAddress());
        existingEmployee.setPosition(employee.getPosition());

        employeeRepo.save(existingEmployee);
        return "Employee updated";
    }

    // delete employee
    public String deleteEmployee(Long id) {
        EmployeeModel existingEmployee = employeeRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
        employeeRepo.delete(existingEmployee);
        return "Employee deleted";
    }
}
