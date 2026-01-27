
package com.example.cellphoneback.dto.response.operation.Operation;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OperationBulkUpsertResponse {
    int createOperation;
    int deleteOperation;
    int updateOperation;

}
