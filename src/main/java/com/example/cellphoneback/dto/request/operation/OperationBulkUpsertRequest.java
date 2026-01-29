package com.example.cellphoneback.dto.request.operation;


import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class OperationBulkUpsertRequest {

    List<Item> operationList;

    @Getter
    @Setter
    static public class Item {
        private String id;
        private String name;
        private String description;
    }
}
