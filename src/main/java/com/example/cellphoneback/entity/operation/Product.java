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
    private Boolean isDeleted;

    @OneToMany(mappedBy = "product")
    private List<ProductRouting> productRoutingList;



    @PrePersist
    public void prePersist() {
        if (this.isDeleted == null) {
            this.isDeleted = false;
        }
    }


}
