
package com.example.cellphoneback.dto.response.simulation;

import com.example.cellphoneback.entity.simulation.Simulation;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class GetAllSimulationResponse {
    List<Item>  simulationScheduleList;

    @Getter
    @Builder
    @JsonPropertyOrder({
            "id",
            "memberName",
            "title",
            "description",
            "productCount",
            "requiredStaff",
            "status",
            "simulationStartDate",
            "workTime"
    })
    public static class Item{
        String id;
        String memberName;
        String title;
        String description;
        int productCount;
        int requiredStaff;
        String status;
        LocalDate simulationStartDate;
        int workTime;


    }
}
