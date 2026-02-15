package com.example.cellphoneback.entity.operation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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
    private Boolean isDeleted;

    @PrePersist
    public void prePersist() {
        if (this.isDeleted == null) {
            this.isDeleted = false;
        }
    }

}
