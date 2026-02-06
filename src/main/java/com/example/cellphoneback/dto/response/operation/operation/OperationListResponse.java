package com.example.cellphoneback.dto.response.operation.operation;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Schema(description = "Operation 목록 응답 DTO")
public class OperationListResponse {

    @Schema(description = "Operation 리스트")
    private List<Item> operationList;

    @Getter
    @Builder
    @Schema(description = "Operation 항목 DTO")
    @JsonPropertyOrder({
            "id",
            "name",
            "description"
    })
    public static class Item {

        @Schema(description = "Operation ID", example = "op-001")
        private String id;

        @Schema(description = "Operation 이름", example = "조립")
        private String name;

        @Schema(description = "Operation 설명", example = "스마트폰 조립 작업")
        private String description;
    }
}