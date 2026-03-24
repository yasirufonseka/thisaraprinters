package com.example.thisaraprinters.dto;

import java.sql.Date;

import org.hibernate.validator.constraints.UniqueElements;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
public class UserDto {

    private String username;
    private String password;
    private Date addeddate;
    private Date updatedate;
    private String note;
    private String userphoto;
    private String status;
    private Long employeeid;

}
