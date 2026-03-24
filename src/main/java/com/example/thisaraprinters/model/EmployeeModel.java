package com.example.thisaraprinters.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "employee")
public class EmployeeModel {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;
    private String fullname;
    private String callingname;
    private String address;
    private String email;
    private String gender;
    private LocalDate dob;
    private String nic;
    private String phonenumber;
    private LocalDate addeddate;
    private LocalDate updateddate;
    @ManyToOne
    @JoinColumn(name = "designation_id", referencedColumnName = "id")
    private DesignationModel designationid;
}
