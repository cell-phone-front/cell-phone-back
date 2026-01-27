package com.example.cellphoneback.dto.request.operation;


import com.example.cellphoneback.entity.operation.Machine;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class MachineBulkUpsertRequest {

    List<Item> machineList;

    @Getter
    @Setter
    static public class Item {
        private String id;
        private String koreanName;
        private String description;


        public Machine toEntity() {
            return Machine.builder()
                    .id(this.id)
                    .koreanName(this.koreanName)
                    .description(this.description)
                    .build();
        }
    }



}
