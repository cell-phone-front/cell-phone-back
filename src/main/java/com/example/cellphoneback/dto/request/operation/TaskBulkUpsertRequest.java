package com.example.cellphoneback.dto.request.operation;


import com.example.cellphoneback.entity.operation.Task;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class TaskBulkUpsertRequest {

    List<Item> taskList;

    @Getter
    @Setter
    static public class Item {
        private String id;
        private String koreanName;
        private String description;


        public Task toEntity() {
            return Task.builder()
                    .id(this.id)
                    .koreanName(this.koreanName)
                    .description(this.description)
                    .build();
        }
    }



}
