
package com.example.cellphoneback.dto.response.simulation;

import com.example.cellphoneback.entity.simulation.Simulation;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateSimulationResponse {
    Simulation simulation;
}
