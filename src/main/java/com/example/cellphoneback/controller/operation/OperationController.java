package com.example.cellphoneback.controller.operation;

import com.example.cellphoneback.dto.request.operation.OperationBulkUpsertRequest;
import com.example.cellphoneback.dto.response.operation.operation.OperationBulkUpsertResponse;
import com.example.cellphoneback.dto.response.operation.operation.OperationListResponse;
import com.example.cellphoneback.dto.response.operation.operation.OperationParseResponse;
import com.example.cellphoneback.entity.member.Member;
import com.example.cellphoneback.service.operation.OperationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Operation", description = "공정 단계 관련 API")
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/operation")
public class OperationController {
    private final OperationService operationService;

    @Operation(summary = "공정 단계 엑셀 파싱", description = "공정 단계 엑셀 파일을 파싱합니다. admin, planner 권한 필요")
    @PostMapping("/xls")
    public ResponseEntity<?> operationXls(@RequestBody MultipartFile file,
                                          @RequestAttribute Member member) {
        OperationParseResponse response = operationService.operationParseService(member, file);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "공정 단계 등록, 수정, 삭제", description = "공정 단계를 등록, 수정, 삭제합니다. admin, planner 권한 필요")
    @PostMapping("/upsert")
    public ResponseEntity<?> operationUpsert(@RequestBody OperationBulkUpsertRequest request,
                                             @RequestAttribute Member member) {
        OperationBulkUpsertResponse response = operationService.operationBulkUpsertService(member, request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @Operation(summary = "공정 단계 전체 조회", description = "모든 공정 단계를 조회합니다. 키워드로 필터링할 수 있습니다. admin, planner 권한 필요")
    @GetMapping
    public ResponseEntity<?> getAllOperations(@RequestAttribute Member member,
                                              @RequestParam(required = false) String keyword) {

        OperationListResponse response = operationService.operationListService(member, keyword);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
