package com.example.cellphoneback.dto.response.simulation;

import com.example.cellphoneback.entity.simulation.SimulationSchedule;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ScheduleSummaryResponse {
    boolean success;
    SimulationSchedule schedule;
    String[] advice;
}
