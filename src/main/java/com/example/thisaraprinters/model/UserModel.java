package com.example.thisaraprinters.model;

import java.sql.Date;
import java.time.LocalDate;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;


@Entity
@Data
@Table(name = "users")
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;
    private String password;
    private LocalDate addeddate;
    private LocalDate updatedate;
    private String note;
    private String userphoto;
    private String status;
    @OneToOne()
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    private EmployeeModel employeeid;

}
