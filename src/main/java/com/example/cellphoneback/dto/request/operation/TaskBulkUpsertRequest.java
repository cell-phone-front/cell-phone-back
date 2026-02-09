package com.example.cellphoneback.dto.request.operation;

import com.example.cellphoneback.entity.operation.Task;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(description = "작업(Task) 일괄 등록/수정 요청 DTO")
public class TaskBulkUpsertRequest {

    @Schema(description = "작업 목록")
    List<Item> taskList;

    @Getter
    @Setter
    @Schema(description = "작업 개별 항목")
    public static class Item {

        @Schema(description = "작업 ID (수정 시 사용, 신규 등록 시 null 가능)", example = "TASK-001")
        private String id;

        @Schema(description = "연결된 공정(Operation) ID", example = "OP-001")
        private String operationId;

        @Schema(description = "사용 설비(Machine) ID", example = "MC-01")
        private String machineId;

        @Schema(description = "작업 이름", example = "PCB 조립")
        private String name;

        @Schema(description = "작업 소요 시간 (분 단위)", example = "60")
        private int duration;

        @Schema(description = "작업 설명", example = "기판에 부품을 장착하는 공정")
        private String description;

        public Task toEntity() {
            return Task.builder()
                    .id(this.id)
                    .name(this.name)
                    .duration(this.duration)
                    .description(this.description)
                    .build();
        }
    }
}