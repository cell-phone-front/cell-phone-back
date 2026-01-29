package com.example.cellphoneback.entity.operation;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Operation {

    @Id
    private String id;

    private String koreanName;
    private int productId;
    private String description;
    private int duration;
}
