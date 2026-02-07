package com.example.cellphoneback.dto.request.operation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(description = "제품 라우팅 일괄 등록/수정 요청 DTO")
public class ProductRoutingBulkUpsertRequest {

    @Schema(description = "제품 라우팅 목록", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<Item> productRoutingList;

    @Getter
    @Setter
    @Schema(description = "제품 라우팅 항목")
    static public class Item {

        @Schema(description = "라우팅 ID")
        private String id;

        @Schema(description = "라우팅 이름")
        private String name;

        @Schema(description = "제품 ID")
        private String productId;

        @Schema(description = "공정 ID")
        private String operationId;

        @Schema(description = "공정 순서")
        private int operationSeq;

        @Schema(description = "설명")
        private String description;
    }
}