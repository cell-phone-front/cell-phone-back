package com.example.cellphoneback.dto.response.simulation;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Schema(description = "API 솔브 결과 응답 DTO")
public class SolveApiResult {

    @Schema(description = "메이크스팬", example = "120")
    private int makespan;

    @Schema(description = "상태", example = "SUCCESS")
    private String status;

    @Schema(description = "작업 스케줄 리스트")
    private List<TaskSchedule> scheduleList;


    @Setter
    @Getter
    @Schema(description = "작업 스케줄 정보")
    static public class TaskSchedule {

        @Schema(description = "제품 ID", example = "prod-123")
        @JsonProperty("product_id")
        private String productId;

        @Schema(description = "작업 ID", example = "task-456")
        @JsonProperty("task_id")
        private String taskId;

        @Schema(description = "작업 시작 시간", example = "0")
        private int start;

        @Schema(description = "작업 종료 시간", example = "30")
        private int end;

    }
}
