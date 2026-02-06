package com.example.cellphoneback.dto.response.operation.productRouting;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Schema(description = "엑셀 파싱된 ProductRouting 목록 응답 DTO")
public class ProductRoutingParseResponse {

    @Schema(description = "파싱된 ProductRouting 리스트")
    private List<xls> productRoutingList;

    @Getter
    @Builder
    @Schema(description = "ProductRouting 항목 DTO")
    @JsonPropertyOrder({"id", "name", "productId", "operationId", "operationSeq", "description"})
    public static class xls {

        @Schema(description = "ProductRouting ID", example = "pr-001")
        private String id;

        @Schema(description = "ProductRouting 이름", example = "스마트폰 조립 라우팅")
        private String name;

        @Schema(description = "Product ID", example = "product-001")
        private String productId;

        @Schema(description = "Operation ID", example = "op-123")
        private String operationId;

        @Schema(description = "Operation 순서", example = "1")
        private int operationSeq;

        @Schema(description = "ProductRouting 설명", example = "스마트폰 A 모델 조립 작업")
        private String description;
    }
}