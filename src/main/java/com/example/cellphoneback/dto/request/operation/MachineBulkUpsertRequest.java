package com.example.cellphoneback.dto.request.operation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(description = "설비(Machine) 일괄 등록/수정 요청 DTO")
public class MachineBulkUpsertRequest {

    @Schema(description = "설비 목록", requiredMode = Schema.RequiredMode.REQUIRED)
    List<Item> machineList;

    @Getter
    @Setter
    @Schema(description = "설비 항목")
    static public class Item {

        @Schema(description = "설비 ID")
        private String id;

        @Schema(description = "설비 이름")
        private String name;

        @Schema(description = "설비 설명")
        private String description;

    }
}