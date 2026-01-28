package com.example.cellphoneback.dto.request.operation;


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

    }



}
