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

    private List<TaskSchedule> schedules;


    @Setter
    @Getter
    static public class TaskSchedule {
        @JsonProperty("job_id")
        private String jobId;
        @JsonProperty("task_id")
        private String taskId;
        @JsonProperty("tool_id")
        private String tool_id;

        private int start;
        private int end;

    }
}
