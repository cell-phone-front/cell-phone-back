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
    private Boolean isDeleted;

    @OneToMany(mappedBy = "operation")
    private List<Task> taskList;

    @PrePersist
    public void prePersist() {
        if (this.isDeleted == null) {
            this.isDeleted = false;
        }
    }


}
