package com.example.cellphoneback.entity.simulation;

import com.example.cellphoneback.entity.member.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Simulation {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    private String title;
    private String description;
    private int requiredStaff;
    private String status;
    private LocalDate simulationStartDate;
    private int workTime;

    @OneToMany(mappedBy = "simulation")
    private List<SimulationProduct> simulationProductList;

    @PrePersist
    public void prePersist() {
        this.id = UUID.randomUUID().toString().substring(0, 6);
        this.status = "대기중";
    }


}
