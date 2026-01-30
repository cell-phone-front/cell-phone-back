
package com.example.cellphoneback.dto.response.operation.machine;

import com.example.cellphoneback.entity.operation.Machine;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MachineListResponse {

    List<Machine> machineList;
}
