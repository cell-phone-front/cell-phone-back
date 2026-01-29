package com.example.cellphoneback.entity.operation;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
    public String productId;
    public String operationId;
    public int operationSeq;
    public String description;
}
