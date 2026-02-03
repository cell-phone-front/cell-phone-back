
package com.example.cellphoneback.dto.response.simulation;

import com.example.cellphoneback.entity.simulation.SimulationSchedule;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;
import org.w3c.dom.Text;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Builder
public class GetSimulationScheduleResponse {
    List<Item> scheduleList;

    @Builder
    @Getter
    @JsonPropertyOrder({
            "id",
            "title",
            "description",
            "productName",
            "requiredStaff",
            "plannerName",
            "workerName",
            "taskId",
            "taskName",
            "machineId",
            "machineName",
            "operationId",
            "operationName",
            "startAt",
            "endAt",
            "aiSummary"
    })
    public static class Item {
        private int id;
        private String title;
        private String description;
        private String productName;
        private int requiredStaff;
        private String plannerName;
        private String workerName;
        private String taskId;
        private String taskName;
        private String machineId;
        private String machineName;
        private String operationId;
        private String operationName;
        private LocalDateTime startAt;
        private LocalDateTime endAt;
        private String aiSummary;

        public static Item fromEntity(SimulationSchedule schedule) {
            return Item.builder()//
                    .id(schedule.getId())//
                    .title(schedule.getSimulation().getTitle())//
                    .description(schedule.getSimulation().getDescription())//
                    .productName(schedule.getSimulation()
                            .getSimulationProductList().stream()
                            .map(e -> e.getProduct().getName())
                            .collect(Collectors.joining(", ")))//
                    .requiredStaff(schedule.getSimulation().getRequiredStaff())//
                    .plannerName(schedule.getPlannerId().getName())//
                    .workerName(schedule.getWorkerId().getName())//
                    .taskId(schedule.getTask().getId())//
                    .taskName(schedule.getTask().getName())//
                    .machineId(schedule.getTask().getMachine().getId())//
                    .machineName(schedule.getTask().getMachine().getName())//
                    .operationId(schedule.getTask().getOperation().getId())//
                    .operationName(schedule.getTask().getOperation().getName())//
                    .startAt(schedule.getStartAt())//
                    .endAt(schedule.getEndAt())//
                    .aiSummary(schedule.getAiSummary())//
                    .build();

        }
    }
}
