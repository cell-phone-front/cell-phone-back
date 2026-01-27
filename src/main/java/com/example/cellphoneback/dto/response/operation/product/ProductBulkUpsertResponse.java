
package com.example.cellphoneback.dto.response.operation.product;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductBulkUpsertResponse {
    int createProduct;
    int deleteProduct;
    int updateProduct;

}
