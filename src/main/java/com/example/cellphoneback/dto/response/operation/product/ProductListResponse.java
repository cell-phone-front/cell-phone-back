package com.example.cellphoneback.dto.response.operation.product;

import com.example.cellphoneback.entity.operation.Product;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ProductListResponse {
    List<Product> productList;
}
