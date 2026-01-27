package com.example.cellphoneback.dto.response.operation.product;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ProductParseResponse {
    List<xls> productList;

    @Getter
    @Builder
    @JsonPropertyOrder({"id", "variety"})
    public static class xls{
        private String id;
        private String variety;
    }
}
