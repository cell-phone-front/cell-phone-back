package com.example.cellphoneback.entity.simulation;

import com.example.cellphoneback.entity.member.Member;
import com.example.cellphoneback.entity.operation.Task;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @ManyToOne
    @JsonIgnore
    private Simulation simulation;

    @ManyToOne
    private Task task;

    @ManyToOne
    private Member plannerId;

    @ManyToOne
    private Member workerId;

    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String aiSummary;


}
