package com.example.thisaraprinters.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EmployeeDto {

    private long id;
    private String fullName;
    private String callingName;
    private String nic;
    private String dob;
    private String gender;
    private String email;
    private String phoneNo;
    private String address;
    private String position;
}
