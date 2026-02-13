package com.example.cellphoneback.controller.operation;

import com.example.cellphoneback.dto.request.operation.ProductRoutingBulkUpsertRequest;

import com.example.cellphoneback.dto.response.operation.productRouting.ProductRoutingBulkUpsertResponse;
import com.example.cellphoneback.dto.response.operation.productRouting.ProductRoutingListResponse;
import com.example.cellphoneback.dto.response.operation.productRouting.ProductRoutingParseResponse;
import com.example.cellphoneback.entity.member.Member;
import com.example.cellphoneback.service.operation.ProductRoutingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@SecurityRequirement(name = "bearerAuth")
@Tag(name = "ProductRouting", description = "프로덕트 라우팅 관련 API")
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/operation/product/routing")
public class ProductRoutingController {
    private final ProductRoutingService productRoutingService;

    @Operation(summary = "프로덕트 라우팅 엑셀 파싱", description = "프로덕트 라우팅 엑셀 파일을 파싱합니다. 관리자(ADMIN), 기획자(PLANNER)만 접근할 수 있습니다.")
    @PostMapping("/xls")
    public ResponseEntity<?> productRoutingXls(@RequestBody MultipartFile file,
                                               @RequestAttribute Member member) {

        ProductRoutingParseResponse response = productRoutingService.productRoutingParseService(member, file);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "프로덕트 라우팅 등록, 수정, 삭제", description = "프로덕트 라우팅을 등록, 수정, 삭제합니다. 관리자(ADMIN), 기획자(PLANNER)만 접근할 수 있습니다.")
    @PostMapping("/upsert")
    public ResponseEntity<?> productRoutingUpsert(@RequestBody ProductRoutingBulkUpsertRequest request,
                                                  @RequestAttribute Member member) {
        ProductRoutingBulkUpsertResponse response = productRoutingService.productRoutingBulkUpsertService(member, request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "프로덕트 라우팅 전체 조회", description = "모든 프로덕트 라우팅을 조회합니다. 키워드로 필터링할 수 있습니다. 관리자(ADMIN), 기획자(PLANNER)만 접근할 수 있습니다.")
    @GetMapping
    public ResponseEntity<?> getAllProductRouting(@RequestAttribute Member member,
                                                  @RequestParam(required = false) String keyword) {
        ProductRoutingListResponse response = productRoutingService.productRoutingListService(member, keyword);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
