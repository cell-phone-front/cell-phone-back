
package com.example.cellphoneback.dto.response.operation;


import com.example.cellphoneback.entity.operation.Task;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class TaskListResponse {

    List<Task> taskList;
}
