package com.example.cellphoneback.dto.response.simulation;

import com.example.cellphoneback.entity.simulation.SimulationSchedule;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class SchedulePersonalResponse {
    List<Item> schedule;

    @Getter
    @Builder
    @JsonPropertyOrder({"productName", "operationName", "taskId", "taskName", "machineId", "machineName", "startAt", "endAt", "shift"})
    public static class Item {
        private String productName;
        private String operationName;
        private String taskId;
        private String taskName;
        private String machineId;
        private String machineName;
        private LocalDateTime startAt;
        private LocalDateTime endAt;

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
