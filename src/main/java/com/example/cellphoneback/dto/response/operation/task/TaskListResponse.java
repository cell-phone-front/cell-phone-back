
package com.example.cellphoneback.dto.response.operation.task;


import com.example.cellphoneback.entity.operation.Task;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class TaskListResponse {

    List<Item> taskList;

    @Getter
    @Builder
    @JsonPropertyOrder({
            "id",
            "operationId",
            "machineId",
            "name",
            "duration",
            "description"
    })
    public static class Item {
        private String id;
        private String operationId;
        private String machineId;
        private String name;
        private int duration;
        private String description;

    }
}
