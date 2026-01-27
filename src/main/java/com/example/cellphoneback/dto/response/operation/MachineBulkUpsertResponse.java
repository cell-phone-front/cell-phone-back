
package com.example.cellphoneback.dto.response.operation;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MachineBulkUpsertResponse {
    int createMachine;
    int deleteMachine;
    int updateMachine;

}
