package com.example.cellphoneback.entity.operation;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Task {

    @Id
    private String id;

    private String operationId;
    private String machineId;
    private String description;

}
