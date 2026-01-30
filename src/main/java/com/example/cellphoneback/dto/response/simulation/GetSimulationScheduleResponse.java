
package com.example.cellphoneback.dto.response.simulation;

import com.example.cellphoneback.entity.simulation.SimulationSchedule;
import lombok.Builder;
import lombok.Getter;

import java.util.Optional;

@Getter
@Builder
public class GetSimulationScheduleResponse {
    Optional<SimulationSchedule> simulationSchedule;
}
