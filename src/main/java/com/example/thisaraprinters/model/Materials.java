package com.example.thisaraprinters.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "materials")
@Data
public class Materials {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "material")
    private String material;
    @Column(name="availablequantity")
    private Integer availablequantity;
    @Column(name="units")
    private String units;
    @Column(name="reorderlevel")
    private Integer reorderlevel;
    @Column(name="status")
    private String status;
    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category categoryid;

    @ToString.Exclude
    @JsonIgnore
    @ManyToMany(mappedBy = "materials")
    private List<Supplier> supplires = new ArrayList<>();

    @ManyToMany(mappedBy = "materialsList")
    private List<QuotationModel> quotations = new ArrayList<>();



}
