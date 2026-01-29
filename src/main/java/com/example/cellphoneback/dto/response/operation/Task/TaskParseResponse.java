
package com.example.cellphoneback.dto.response.operation.Task;

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
    @JsonPropertyOrder({"id", "operationId", "machineId", "name", "duration" ,"description"})
    public static class xls {
        private String id;
        private String operationId;
        private String machineId;
        private String name;
        private int duration;
        private String description;
    }
}
