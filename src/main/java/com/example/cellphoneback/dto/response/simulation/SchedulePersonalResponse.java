package com.example.cellphoneback.dto.response.simulation;

import com.example.cellphoneback.entity.simulation.SimulationSchedule;
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
    public static class Item {
        private String taskId;
        private String taskName;
        private String productName;
        private String machineId;
        private String machineName;
        private String operationName;
        private LocalDateTime startAt;
        private LocalDateTime endAt;

        public static Item fromEntity(SimulationSchedule schedule) {
            return Item.builder()
                    .taskId(schedule.getTask().getId())
                    .taskName(schedule.getTask().getName())
                    .productName(schedule.getSimulation().getSimulationProductList().stream()
                            .map(e -> e.getProduct().getName())
                            .collect(Collectors.joining(", ")))
                    .machineId(schedule.getTask().getMachine().getId())
                    .machineName(schedule.getTask().getMachine().getName())
                    .operationName(schedule.getTask().getOperation().getName())
                    .startAt(schedule.getStartAt())
                    .endAt(schedule.getEndAt())
                    .build();
        }
    }
}
