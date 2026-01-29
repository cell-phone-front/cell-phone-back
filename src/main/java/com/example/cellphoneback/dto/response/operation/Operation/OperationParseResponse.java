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
    @JsonPropertyOrder({"id", "name", "description"})
    public static class xls{
        private String id;
        private String name;
        private String description;
    }
}
