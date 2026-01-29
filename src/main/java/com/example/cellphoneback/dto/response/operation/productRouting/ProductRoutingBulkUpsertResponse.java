
package com.example.cellphoneback.dto.response.operation.productRouting;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductRoutingBulkUpsertResponse {
    int createProductRouting;
    int deleteProductRouting;
    int updateProductRouting;

}
