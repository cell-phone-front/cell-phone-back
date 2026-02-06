package com.example.cellphoneback.dto.response.operation.machine;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Schema(description = "엑셀 파싱된 Machine 목록 응답 DTO")
public class MachineParseResponse {

    @Schema(description = "파싱된 Machine 리스트")
    private List<xls> machineList;

    @Getter
    @Builder
    @Schema(description = "Machine 항목 DTO")
    @JsonPropertyOrder({"id", "name", "description"})
    public static class xls {

        @Schema(description = "Machine ID", example = "machine-001")
        private String id;

        @Schema(description = "Machine 이름", example = "조립기 1호기")
        private String name;

        @Schema(description = "Machine 설명", example = "스마트폰 조립용 설비")
        private String description;
    }
}