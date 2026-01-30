package com.example.cellphoneback.entity.operation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRouting {
    @Id
    public String id;
    public String name;

    @ManyToOne
    @JsonIgnore
    public Product product;

    @ManyToOne
    public Operation operation;

    public int operationSeq;
    public String description;
}
