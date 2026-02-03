
package com.example.cellphoneback.dto.response.operation.machine;

import com.example.cellphoneback.entity.operation.Machine;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MachineListResponse {

    List<Item> machineList;

    @Getter
    @Builder
    @JsonPropertyOrder({
            "id",
            "name",
            "description"
    })
    public static class Item {
        private String id;
        private String name;
        private String description;
    }
}
