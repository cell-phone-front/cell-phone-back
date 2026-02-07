package com.example.cellphoneback.dto.request.simulation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Schema(description = "시뮬레이션 생성 요청 DTO")
public class CreateSimulationRequest {

    @Schema(description = "시뮬레이션 제목", example = "3월 판매 시뮬레이션")
    String title;

    @Schema(description = "시뮬레이션 설명", example = "3월 한 달간 판매 인력 배치 시뮬레이션입니다.")
    String description;

    @Schema(description = "필요 인원 수", example = "5")
    int requiredStaff;

    @Schema(description = "시뮬레이션 시작 날짜", example = "2026-03-01")
    LocalDate simulationStartDate;

    @Schema(description = "상품 목록", example = "[\"갤럭시 S25\", \"아이폰 17\"]")
    List<String> productList;

    @Schema(description = "1인당 근무 시간 (분 단위)", example = "480")
    int workTime;
}