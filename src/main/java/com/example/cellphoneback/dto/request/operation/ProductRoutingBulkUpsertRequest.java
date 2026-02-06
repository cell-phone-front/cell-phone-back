package com.example.cellphoneback.dto.request.operation;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class ProductRoutingBulkUpsertRequest {

    @Schema(description = "제품 라우팅 목록", required = true)
    private List<Item> productRoutingList;

    @Getter
    @Setter
    static public class Item {
        private String id;
        private String name;
        private String productId;
        private String operationId;
        private int operationSeq;
        private String description;
    }
}
