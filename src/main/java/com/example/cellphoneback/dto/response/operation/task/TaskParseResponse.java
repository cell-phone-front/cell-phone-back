package com.example.cellphoneback.dto.response.operation.task;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Schema(description = "엑셀 파싱된 Task 목록 응답 DTO")
public class TaskParseResponse {

    @Schema(description = "파싱된 Task 리스트")
    private List<xls> taskList;

    @Getter
    @Builder
    @Schema(description = "Task 항목 DTO")
    @JsonPropertyOrder({"id", "operationId", "machineId", "name", "duration", "description"})
    public static class xls {

        @Schema(description = "Task ID", example = "task-001")
        private String id;

        @Schema(description = "Operation ID", example = "op-123")
        private String operationId;

        @Schema(description = "Machine ID", example = "machine-001")
        private String machineId;

        @Schema(description = "Task 이름", example = "스마트폰 조립")
        private String name;

        @Schema(description = "작업 소요 시간(분)", example = "60")
        private Integer duration;

        @Schema(description = "Task 설명", example = "스마트폰 A 모델 조립 작업")
        private String description;
    }
}