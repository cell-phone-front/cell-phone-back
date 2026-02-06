package com.example.cellphoneback.dto.response.operation.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "Product 일괄 등록/수정/삭제 응답 DTO")
public class ProductBulkUpsertResponse {

    @Schema(description = "생성된 Product 개수", example = "5")
    private int createProduct;

    @Schema(description = "삭제된 Product 개수", example = "2")
    private int deleteProduct;

    @Schema(description = "수정된 Product 개수", example = "3")
    private int updateProduct;
}