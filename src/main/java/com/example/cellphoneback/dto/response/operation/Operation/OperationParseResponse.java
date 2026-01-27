package com.example.cellphoneback.dto.response.operation.Operation;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class OperationParseResponse {
    List<xls> operationList;

    @Getter
    @Builder
    @JsonPropertyOrder({"id", "koreanName", "productId", "description", "duration"})
    public static class xls{
        private String id;
        private String koreanName;
        private String productId;
        private String description;
        private double duration;
    }
}
