
package com.example.cellphoneback.dto.response.simulation;

import com.example.cellphoneback.entity.simulation.Simulation;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Getter
@Builder
public class CreateSimulationResponse {

    item simulation;


    @Getter
    @Builder
    @JsonPropertyOrder({
            "id",
            "memberId",
            "title",
            "description",
            "requiredStaff",
            "status",
            "simulationStartDate",
            "productList",
            "workTime"
    })
    public static class item {
        String id;
        String memberId;
        String title;
        String description;
        int requiredStaff;
        String status;
        LocalDate simulationStartDate;
        List<String> productList;
        int workTime;
    }

}
