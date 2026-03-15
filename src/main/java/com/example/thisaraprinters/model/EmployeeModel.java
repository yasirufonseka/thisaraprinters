package com.example.thisaraprinters.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@Entity
@Table(name = "employee")
public class EmployeeModel {


    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private String fullname;
    private String callingname;
    private String nic;
    private String dob;
    private String gender;
    private String email;
    private String phonenumber;
    private String address;
    private String position;
    @DateTimeFormat
    private Date addedDate;
}
