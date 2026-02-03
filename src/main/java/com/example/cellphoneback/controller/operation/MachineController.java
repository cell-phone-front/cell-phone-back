package com.example.cellphoneback.controller.operation;

import com.example.cellphoneback.dto.request.operation.MachineBulkUpsertRequest;
import com.example.cellphoneback.dto.response.operation.machine.MachineBulkUpsertResponse;
import com.example.cellphoneback.dto.response.operation.machine.MachineListResponse;
import com.example.cellphoneback.dto.response.operation.machine.MachineParseResponse;
import com.example.cellphoneback.entity.member.Member;
import com.example.cellphoneback.service.operation.MachineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

//@SecurityRequirement(name = "bearerAuth")
//@Tag(name = "Comment", description = "댓글 관련 API")
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/operation/machine")
public class MachineController {
    private final MachineService machineService;


    // POST	/api/operation/machine/xls	기계 엑셀 파싱	admin,planner
    @PostMapping("/xls")
    public ResponseEntity<MachineParseResponse> machineParse(@RequestAttribute Member member,
                                                             @RequestBody MultipartFile file) {

        MachineParseResponse response = machineService.machineParseService(member, file);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    // POST	/api/operation/machine/upsert	기계 등록, 수정, 삭제	admin,planner
    @PostMapping("/upsert")
    public ResponseEntity<MachineBulkUpsertResponse> machineUpsert(@RequestAttribute Member member,
                                                                   @RequestBody MachineBulkUpsertRequest request){

        MachineBulkUpsertResponse response = machineService.machineUpsertService(member, request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    // GET	/api/operation/machine	기계 전체 조회	admin,planner
    @GetMapping
    public ResponseEntity<MachineListResponse> machineList(@RequestAttribute Member member,
                                                           @RequestBody(required = false) String keyword){

        MachineListResponse response = machineService.machineListService(member, keyword);

       return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
