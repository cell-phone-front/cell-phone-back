
package com.example.cellphoneback.dto.response.simulation;

import com.example.cellphoneback.entity.simulation.Simulation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "시뮬레이션 조회 응답 DTO")
public class GetSimulationResponse {

    @Schema(description = "시뮬레이션 정보", example = "Simulation 객체")
    private Simulation simulation;
}
