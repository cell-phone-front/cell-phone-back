
package com.example.cellphoneback.dto.response.operation;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class TaskParseResponse {

    List<xls>  taskList;

    @Getter
    @Builder
    @JsonPropertyOrder({"id", "koreanName", "operationId", "machineId", "description"})
    public static class xls {
        private String id;
        private String koreanName;
        private String operationId;
        private String machineId;
        private String description;
    }
}
