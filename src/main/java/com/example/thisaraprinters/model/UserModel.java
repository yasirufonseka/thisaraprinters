package com.example.thisaraprinters.model;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;


@Entity
@Data
@Table(name = "users")
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;
    @JsonIgnore
    private String password;
    private LocalDate addeddate;
    private LocalDate updatedate;
    private String note;
    private String userphoto;
    private String status;
    @OneToOne()
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    private EmployeeModel employeeid;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "users_has_role",
        joinColumns = @JoinColumn(name="users_id"),
        inverseJoinColumns = @JoinColumn(name="role_id")
    )
    private List<RoleModel> roles;

}
