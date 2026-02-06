package com.example.cellphoneback.dto.response.simulation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "시뮬레이션 삭제 응답 DTO")
public class DeleteSimulationResponse {

    @Schema(description = "삭제 결과 메시지", example = "시뮬레이션이 성공적으로 삭제되었습니다.")
    private String message;
}