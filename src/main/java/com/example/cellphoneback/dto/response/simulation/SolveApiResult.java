package com.example.cellphoneback.dto.response.simulation;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class SolveApiResult {
    private int makespan;
    private String status;

    private List<TaskSchedule> scheduleList;


    @Setter
    @Getter
    static public class TaskSchedule {
        @JsonProperty("product_id")
        private String productId;
        @JsonProperty("task_id")
        private String taskId;

        private int start;
        private int end;

    }
}
