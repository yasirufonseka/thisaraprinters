package com.example.thisaraprinters.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EmployeeDto {

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
    private Integer designationid;

}
