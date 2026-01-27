
package com.example.cellphoneback.dto.response.operation.Machine;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MachineParseResponse {

    List<xls>  machineList;

    @Getter
    @Builder
    @JsonPropertyOrder({"id", "koreanName", "description"})
    public static class xls {
        private String id;
        private String koreanName;
        private String description;
    }
}
