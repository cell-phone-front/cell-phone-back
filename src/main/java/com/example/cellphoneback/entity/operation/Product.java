package com.example.cellphoneback.entity.operation;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Product {

    @Id
    private String id;

    private String brand;
    private String name;
    private String description;

    @OneToMany(mappedBy = "product")
    private List<ProductRouting> ProductRoutingList;


}
