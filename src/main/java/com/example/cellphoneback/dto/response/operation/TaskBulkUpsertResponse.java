
package com.example.cellphoneback.dto.response.operation;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TaskBulkUpsertResponse {
    int createTask;
    int deleteTask;
    int updateTask;

}
