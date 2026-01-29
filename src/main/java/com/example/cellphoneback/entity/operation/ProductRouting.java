package com.example.cellphoneback.entity.operation;

import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRouting {
    @Id
    public String id;
    public String name;
    public String product_id;
    public String operation_id;
    public int operation_seq;
    public String description;
}
