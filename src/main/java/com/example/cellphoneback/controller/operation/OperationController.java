package com.example.cellphoneback.controller.operation;

import com.example.cellphoneback.dto.request.operation.OperationBulkUpsertRequest;
import com.example.cellphoneback.dto.response.operation.Operation.OperationBulkUpsertResponse;
import com.example.cellphoneback.dto.response.operation.Operation.OperationListResponse;
import com.example.cellphoneback.dto.response.operation.Operation.OperationParseResponse;
import com.example.cellphoneback.entity.member.Member;
import com.example.cellphoneback.service.operation.OperationService;
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
@RequestMapping("/api/operation")
public class OperationController {
    private final OperationService operationService;

//    operation	POST	/api/operation/xls	공정 단계 엑셀 파싱	admin, planner
    @PostMapping("/xls")
    public ResponseEntity<?> operationXls(@RequestBody MultipartFile file,
                                          @RequestAttribute Member member) {
        OperationParseResponse response = operationService.operationParseService(member, file);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

//    operation	POST	/api/operation/upsert	공정 단계 추가, 수정, 삭제	admin, planner
    @PostMapping("/upsert")
    public ResponseEntity<?> operationUpsert(@RequestBody OperationBulkUpsertRequest request,
                                             @RequestAttribute Member member) {
        OperationBulkUpsertResponse response = operationService.operationBulkUpsertService(member, request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    // 	operation	GET	/api/operation	공정 단계 전체 조회	admin, planner
    @GetMapping
    public ResponseEntity<?> getAllOperations(@RequestAttribute Member member) {

        OperationListResponse response = operationService.operationListService(member);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
