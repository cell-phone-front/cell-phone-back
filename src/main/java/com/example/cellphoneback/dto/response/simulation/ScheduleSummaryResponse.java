package com.example.cellphoneback.dto.response.simulation;

import com.example.cellphoneback.entity.simulation.SimulationSchedule;
import lombok.Builder;
import lombok.Getter;
import org.w3c.dom.Text;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ScheduleSummaryResponse {
    boolean success;
    List<GetSimulationScheduleResponse.Item> schedule;
    String[] advice;



}
