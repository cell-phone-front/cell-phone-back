package com.example.cellphoneback.dto.response.simulation;

import com.example.cellphoneback.entity.simulation.Simulation;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@Schema(description = "모든 시뮬레이션 조회 응답 DTO")
public class GetAllSimulationResponse {

    @Schema(description = "시뮬레이션 스케줄 리스트")
    private List<Item> simulationScheduleList;

    @Getter
    @Builder
    @Schema(description = "시뮬레이션 항목 DTO")
    @JsonPropertyOrder({
            "id",
            "memberName",
            "title",
            "description",
            "productCount",
            "requiredStaff",
            "status",
            "simulationStartDate",
            "workTime",
            "aiSummary"
    })
    public static class Item {

        @Schema(description = "시뮬레이션 ID", example = "sim-001")
        private String id;

        @Schema(description = "작성자 이름", example = "홍길동")
        private String memberName;

        @Schema(description = "시뮬레이션 제목", example = "스마트폰 조립 시뮬레이션")
        private String title;

        @Schema(description = "시뮬레이션 설명", example = "A팀의 스마트폰 조립 시뮬레이션")
        private String description;

        @Schema(description = "제품 수", example = "5")
        private int productCount;

        @Schema(description = "필요 인원 수", example = "3")
        private int requiredStaff;

        @Schema(description = "시뮬레이션 상태", example = "COMPLETED")
        private String status;

        @Schema(description = "시뮬레이션 시작 날짜", example = "2026-02-06")
        private LocalDate simulationStartDate;

        @Schema(description = "작업 시간 (분)", example = "480")
        private int workTime;

        @Schema(description = "AI 요약", example = "효율적인 작업 배치 완료")
        private String aiSummary;
    }
}