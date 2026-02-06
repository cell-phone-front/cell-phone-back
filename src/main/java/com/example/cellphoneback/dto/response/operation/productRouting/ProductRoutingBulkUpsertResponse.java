package com.example.cellphoneback.dto.response.operation.productRouting;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "ProductRouting 일괄 등록/수정/삭제 응답 DTO")
public class ProductRoutingBulkUpsertResponse {

    @Schema(description = "생성된 ProductRouting 개수", example = "5")
    private int createProductRouting;

    @Schema(description = "삭제된 ProductRouting 개수", example = "2")
    private int deleteProductRouting;

    @Schema(description = "수정된 ProductRouting 개수", example = "3")
    private int updateProductRouting;
}