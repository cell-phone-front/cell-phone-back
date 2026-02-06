package com.example.cellphoneback.dto.response.simulation;

import com.example.cellphoneback.entity.simulation.SimulationSchedule;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@Schema(description = "개인별 스케줄 조회 응답 DTO")
public class SchedulePersonalResponse {

    @Schema(description = "스케줄 목록")
    List<Item> schedule;

    @Getter
    @Builder
    @Schema(description = "스케줄 항목 DTO")
    @JsonPropertyOrder({"productName", "operationName", "taskId", "taskName", "machineId", "machineName", "startAt", "endAt", "shift"})
    public static class Item {

        @Schema(description = "제품명", example = "스마트폰 A, 스마트폰 B")
        private String productName;

        @Schema(description = "작업명", example = "조립")
        private String operationName;

        @Schema(description = "작업 ID", example = "task-12345")
        private String taskId;

        @Schema(description = "작업명", example = "스마트폰 조립 작업")
        private String taskName;

        @Schema(description = "설비 ID", example = "machine-67890")
        private String machineId;

        @Schema(description = "설비명", example = "조립기 1호기")
        private String machineName;

        @Schema(description = "작업 시작 시간", example = "2024-07-01T08:00:00")
        private LocalDateTime startAt;

        @Schema(description = "작업 종료 시간", example = "2024-07-01T16:00:00")
        private LocalDateTime endAt;

        @Schema(description = "근무조 (D: 주간, E: 야간, N: 심야)", example = "D")
        private String shift;

        public static Item fromEntity(SimulationSchedule schedule) {
            LocalDateTime start = schedule.getStartAt();
            LocalDateTime end = schedule.getEndAt();

            return Item.builder()
                    .productName(schedule.getSimulation().getSimulationProductList().stream()
                            .map(e -> e.getProduct().getName())
                            .collect(Collectors.joining(", ")))
                    .operationName(schedule.getTask().getOperation().getName())
                    .taskId(schedule.getTask().getId())
                    .taskName(schedule.getTask().getName())
                    .machineId(schedule.getTask().getMachine().getId())
                    .machineName(schedule.getTask().getMachine().getName())
                    .startAt(schedule.getStartAt())
                    .endAt(schedule.getEndAt())
                    .shift(calculateShift(start, end))
                    .build();
        }

        // 근무 시작 시간 기준으로 D/E/N 계산
        private static String calculateShift(LocalDateTime start, LocalDateTime end) {
            if (start == null) return "-";

            int hour = start.getHour();
            if (hour >= 8 && hour < 16) return "D";
            if (hour >= 16 && hour < 24) return "E";
            if (hour >= 0 && hour < 8) return "N";
            return "-";
        }
    }
}
