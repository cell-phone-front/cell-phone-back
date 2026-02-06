package com.example.cellphoneback.dto.response.operation.product;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Schema(description = "엑셀 파싱된 Product 목록 응답 DTO")
public class ProductParseResponse {

    @Schema(description = "파싱된 Product 리스트")
    private List<xls> productList;

    @Getter
    @Builder
    @Schema(description = "Product 항목 DTO")
    @JsonPropertyOrder({"id", "brand", "name", "description"})
    public static class xls {

        @Schema(description = "Product ID", example = "product-001")
        private String id;

        @Schema(description = "브랜드명", example = "삼성")
        private String brand;

        @Schema(description = "Product 이름", example = "스마트폰 A")
        private String name;

        @Schema(description = "Product 설명", example = "스마트폰 A 모델 설명")
        private String description;
    }
}