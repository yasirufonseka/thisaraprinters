package com.example.thisaraprinters.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class UserDto {
    
    private String username;
    private String password;
    private LocalDate addeddate;
    private LocalDate updatedate;
    private String note;
    private String userphoto;
    private String status;
    private Integer employeeid;
    private List<Integer> roleIds;
    private String email;

}
