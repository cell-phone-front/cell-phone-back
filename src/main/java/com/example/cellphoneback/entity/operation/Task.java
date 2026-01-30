package com.example.cellphoneback.entity.operation;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private Operation operation;

    @ManyToOne
    private Machine machine;

    private String name;
    private int duration;
    private String description;

}
