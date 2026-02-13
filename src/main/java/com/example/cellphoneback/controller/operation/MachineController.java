package com.example.cellphoneback.controller.operation;

import com.example.cellphoneback.dto.request.operation.MachineBulkUpsertRequest;
import com.example.cellphoneback.dto.response.operation.machine.MachineBulkUpsertResponse;
import com.example.cellphoneback.dto.response.operation.machine.MachineListResponse;
import com.example.cellphoneback.dto.response.operation.machine.MachineParseResponse;
import com.example.cellphoneback.entity.member.Member;
import com.example.cellphoneback.service.operation.MachineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Machine", description = "기계 관련 API")
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/operation/machine")
public class MachineController {
    private final MachineService machineService;


    @Operation(summary = "기계 엑셀 파싱", description = "기계 엑셀 파일을 파싱합니다. 관리자(ADMIN), 기획자(PLANNER)만 접근할 수 있습니다.")
    @PostMapping("/xls")
    public ResponseEntity<MachineParseResponse> machineParse(@RequestAttribute Member member,
                                                             @RequestBody MultipartFile file) {

        MachineParseResponse response = machineService.machineParseService(member, file);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @Operation(summary = "기계 일괄 생성, 수정, 삭제", description = "기계 정보를 일괄적으로 생성, 수정, 삭제합니다. 관리자(ADMIN), 기획자(PLANNER)만 접근할 수 있습니다.")
    @PostMapping("/upsert")
    public ResponseEntity<MachineBulkUpsertResponse> machineUpsert(@RequestAttribute Member member,
                                                                   @RequestBody MachineBulkUpsertRequest request){

        MachineBulkUpsertResponse response = machineService.machineUpsertService(member, request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @Operation(summary = "기계 목록 조회", description = "기계 목록을 조회합니다. 키워드로 필터링할 수 있습니다. 관리자(ADMIN), 기획자(PLANNER)만 접근할 수 있습니다.")
    @GetMapping
    public ResponseEntity<MachineListResponse> machineList(@RequestAttribute Member member,
                                                           @RequestParam(required = false) String keyword){

        MachineListResponse response = machineService.machineListService(member, keyword);

       return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
