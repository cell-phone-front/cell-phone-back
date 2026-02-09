
package com.example.cellphoneback.dto.response.simulation;

import com.example.cellphoneback.entity.simulation.SimulationSchedule;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.w3c.dom.Text;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Builder
@Schema(description = "시뮬레이션 스케줄 조회 응답 DTO")
public class GetSimulationScheduleResponse {

    @Schema(description = "시뮬레이션 스케줄 리스트", example = "[]")
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
            "operationSeq",
            "startAt",
            "endAt"
    })
    public static class Item {

        @Schema(description = "스케줄 ID", example = "1")
        private int id;

        @Schema(description = "시뮬레이션 제목", example = "스마트폰 조립 시뮬레이션")
        private String title;

        @Schema(description = "시뮬레이션 설명", example = "A팀의 스마트폰 조립 시뮬레이션")
        private String description;

        @Schema(description = "제품명", example = "스마트폰 A, 스마트폰 B")
        private String productName;

        @Schema(description = "필요 인원 수", example = "3")
        private int requiredStaff;

        @Schema(description = "담당 플래너 이름", example = "홍길동")
        private String plannerName;

        @Schema(description = "작업자 이름", example = "김철수")
        private String workerName;

        @Schema(description = "태스크 ID", example = "task-12345")
        private String taskId;

        @Schema(description = "태스크 이름", example = "스마트폰 조립 작업")
        private String taskName;

        @Schema(description = "설비 ID", example = "machine-67890")
        private String machineId;

        @Schema(description = "설비 이름", example = "조립기 1호기")
        private String machineName;

        @Schema(description = "작업의 오퍼레이션 ID", example = "op-98765")
        private String operationId;

        @Schema(description = "작업의 오퍼레이션 이름", example = "조립")
        private String operationName;

        @Schema(description = "작업의 오페레이션 순서")
        private int operationSeq;

        @Schema(description = "작업 시작 시간", example = "2024-07-01T08:00:00")
        private LocalDateTime startAt;

        @Schema(description = "작업 종료 시간", example = "2024-07-01T16:00:00")
        private LocalDateTime endAt;

        @Schema(description = "AI 스케줄링 요약", example = "효율적인 작업 배치 완료")
        private String aiSummary;

        public static Item fromEntity(SimulationSchedule schedule) {
            return Item.builder()//
                    .id(schedule.getId())//
                    .title(schedule.getSimulation().getTitle())//
                    .description(schedule.getSimulation().getDescription())//
                    .productName(schedule.getProduct().getName())//
                    .requiredStaff(schedule.getSimulation().getRequiredStaff())//
                    .plannerName(schedule.getPlannerId().getName())//
                    .workerName(schedule.getWorkerId().getName())//
                    .taskId(schedule.getTask().getId())//
                    .taskName(schedule.getTask().getName())//
                    .machineId(schedule.getTask().getMachine().getId())//
                    .machineName(schedule.getTask().getMachine().getName())//
                    .operationId(schedule.getTask().getOperation().getId())//
                    .operationName(schedule.getTask().getOperation().getName())//
                    .operationSeq(schedule.getProduct().getProductRoutingList().stream()
                            .filter(p -> p.getOperation() != null)
                            .filter(p -> p.getOperation().getId().equals(schedule.getTask().getOperation().getId()))
                            .mapToInt(p -> p.getOperationSeq())
                            .findFirst().orElse(0))//
                    .startAt(schedule.getStartAt())//
                    .endAt(schedule.getEndAt())//
                    .build();

        }
    }
}
