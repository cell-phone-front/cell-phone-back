package com.example.cellphoneback.entity.simulation;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Simulation {

    @Id
    private String id;

    private String memberId;
    private String title;
    private String description;
    private double workTime;
    private int requiredStaff;
    private String aiSummary;
    private LocalDateTime createdStartAt;
    private LocalDateTime createdEndAt;

}
