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
public class Operation {

    @Id
    private String id;

    private String name;
    private String description;

    @OneToMany(mappedBy = "operation")
    private List<Task> taskList;

}
