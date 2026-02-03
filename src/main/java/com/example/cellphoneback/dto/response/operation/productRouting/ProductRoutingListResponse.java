package com.example.cellphoneback.dto.response.operation.productRouting;

import com.example.cellphoneback.entity.operation.ProductRouting;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ProductRoutingListResponse {
    List<Item> productRoutingList;

    @Getter
    @Builder
    @JsonPropertyOrder({
            "id",
            "name",
            "productId",
            "operationId",
            "operationSeq",
            "description"
    })
    public static class Item{
        private String id;
        private String name;
        private String productId;
        private String operationId;
        private int operationSeq;
        private String description;
    }
}
