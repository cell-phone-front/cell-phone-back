
package com.example.cellphoneback.dto.response.simulation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "시뮬레이션 실행 응답 DTO")
public class RunSimulationResponse {

    @Schema(description = "시뮬레이션 상태", example = "COMPLETED")
    String status;

    @Schema(description = "AI 요약 내용", example = "이 시뮬레이션은 성공적으로 완료되었습니다.")
    String aiSummary;
}
