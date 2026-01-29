package com.example.cellphoneback.entity.simulation;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class SimulationSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String simulationId;
    private String taskId;
    private String plannerId;
    private String workerId;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String aiSummary;


}
