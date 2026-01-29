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
public class Task {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn (name = "operation_id")
    private Operation operation;

    @ManyToOne
    @JoinColumn (name = "machine_id")
    private Machine machine;

    private String name;
    private int duration;
    private String description;

}
