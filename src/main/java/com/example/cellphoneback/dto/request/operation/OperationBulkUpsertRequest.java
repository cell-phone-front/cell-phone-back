package com.example.cellphoneback.dto.request.operation;


import com.example.cellphoneback.entity.operation.Operation;
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
        private String koreanName;
        private String productId;
        private String description;
        private double duration;


        public Operation toEntity() {
            return Operation.builder()
                    .id(this.id)
                    .koreanName(this.koreanName)
                    .productId(this.productId)
                    .description(this.description)
                    .duration(this.duration)
                    .build();
        }
    }
}
