package com.example.cellphoneback.dto.request.simulation;

import com.example.cellphoneback.entity.member.Member;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class CreateSimulationRequest {
    String title;
    String description;
    int requiredStaff;
    LocalDate simulationStartDate;
    List<String> productList;
    int workTime;
}
