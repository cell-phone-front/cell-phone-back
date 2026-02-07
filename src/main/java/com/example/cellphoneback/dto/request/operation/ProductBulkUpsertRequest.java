package com.example.cellphoneback.dto.request.operation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(description = "제품 일괄 등록/수정 요청 DTO")
public class ProductBulkUpsertRequest {

    @Schema(description = "제품 목록", requiredMode = Schema.RequiredMode.REQUIRED)
    List<Item> productList;

    @Getter
    @Setter
    @Schema(description = "제품 항목")
    static public class Item {

        @Schema(description = "제품 ID")
        private String id;

        @Schema(description = "브랜드명")
        private String brand;

        @Schema(description = "제품명")
        private String name;

        @Schema(description = "제품 설명")
        private String description;
    }
}