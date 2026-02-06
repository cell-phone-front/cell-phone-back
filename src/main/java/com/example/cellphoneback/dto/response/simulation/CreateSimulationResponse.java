package com.example.cellphoneback.dto.response.simulation;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@Schema(description = "시뮬레이션 생성 응답 DTO")
public class CreateSimulationResponse {

    @Schema(description = "생성된 시뮬레이션 정보")
    item simulation;

    @Getter
    @Builder
    @JsonPropertyOrder({
            "id",
            "memberName",
            "title",
            "description",
            "requiredStaff",
            "status",
            "simulationStartDate",
            "productList",
            "workTime"
    })
    @Schema(description = "시뮬레이션 상세 DTO")
    public static class item {

        @Schema(description = "시뮬레이션 ID", example = "sim-12345")
        String id;

        @Schema(description = "시뮬레이션 생성자 이름", example = "totoro")
        String memberName;

        @Schema(description = "시뮬레이션 제목", example = "스마트폰 생산 계획")
        String title;

        @Schema(description = "시뮬레이션 설명", example = "2026년 2월 스마트폰 생산 시뮬레이션")
        String description;

        @Schema(description = "필요 인원 수", example = "5")
        int requiredStaff;

        @Schema(description = "시뮬레이션 상태", example = "IN_PROGRESS")
        String status;

        @Schema(description = "시뮬레이션 시작일", example = "2026-02-06")
        LocalDate simulationStartDate;

        @Schema(description = "시뮬레이션 대상 제품 목록", example = "[\"제품A\", \"제품B\"]")
        List<String> productList;

        @Schema(description = "총 작업 시간 (분)", example = "480")
        int workTime;
    }
}