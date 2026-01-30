
package com.example.cellphoneback.dto.response.operation.machine;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MachineBulkUpsertResponse {
    int createMachine;
    int deleteMachine;
    int updateMachine;

}
