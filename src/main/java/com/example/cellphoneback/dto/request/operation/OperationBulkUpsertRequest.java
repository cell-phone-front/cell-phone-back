package com.example.cellphoneback.dto.request.operation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(description = "공정(Operation) 일괄 등록/수정 요청 DTO")
public class OperationBulkUpsertRequest {

    @Schema(description = "공정 목록", requiredMode = Schema.RequiredMode.REQUIRED)
    List<Item> operationList;

    @Getter
    @Setter
    @Schema(description = "공정 항목")
    static public class Item {

        @Schema(description = "공정 ID")
        private String id;

        @Schema(description = "공정 이름")
        private String name;

        @Schema(description = "공정 설명")
        private String description;
    }
}