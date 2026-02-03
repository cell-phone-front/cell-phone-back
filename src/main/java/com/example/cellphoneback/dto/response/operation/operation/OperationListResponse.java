package com.example.cellphoneback.dto.response.operation.operation;

import com.example.cellphoneback.entity.operation.Operation;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class OperationListResponse {
    List<Item> operationList;

    @Getter
    @Builder
    @JsonPropertyOrder({
            "id",
            "name",
            "description"
    })
    public static class Item{
        private String id;
        private String name;
        private String description;

    }

}
