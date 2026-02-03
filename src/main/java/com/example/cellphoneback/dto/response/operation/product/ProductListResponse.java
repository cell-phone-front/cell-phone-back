package com.example.cellphoneback.dto.response.operation.product;

import com.example.cellphoneback.entity.operation.Product;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ProductListResponse {
    List<Item> productList;

    @Getter
    @Builder
    @JsonPropertyOrder({
            "id",
            "brand",
            "name",
            "description"
    })
    public static class Item {
        private String id;
        private String brand;
        private String name;
        private String description;
    }
}
