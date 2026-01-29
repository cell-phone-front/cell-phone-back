package com.example.cellphoneback.dto.response.operation.productRouting;

import com.example.cellphoneback.entity.operation.ProductRouting;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ProductRoutingListResponse {
    List<ProductRouting> productRoutingList;
}
