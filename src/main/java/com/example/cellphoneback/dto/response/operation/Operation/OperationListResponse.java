package com.example.cellphoneback.dto.response.operation.Operation;

import com.example.cellphoneback.entity.operation.Operation;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class OperationListResponse {
    List<Operation> operationList;
}
