package com.example.cellphoneback.controller.operation;

import com.example.cellphoneback.dto.request.operation.ProductBulkUpsertRequest;
import com.example.cellphoneback.dto.response.operation.product.ProductBulkUpsertResponse;
import com.example.cellphoneback.dto.response.operation.product.ProductListResponse;
import com.example.cellphoneback.dto.response.operation.product.ProductParseResponse;
import com.example.cellphoneback.entity.member.Member;
import com.example.cellphoneback.service.operation.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Product", description = "생산 대상 관련 API")
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/operation/product")
public class ProductController {
    private final ProductService productService;


    @Operation(summary = "생산 대상 엑셀 파싱", description = "생산 대상 엑셀 파일을 파싱합니다. 관리자(ADMIN), 기획자(PLANNER)만 접근할 수 있습니다.")
    @PostMapping("/xls")
    public ResponseEntity<ProductParseResponse> productXls(@RequestBody MultipartFile file,
                                        @RequestAttribute Member member) {

       ProductParseResponse response = productService.productParseService(member, file);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @Operation(summary = "생산 대상 등록, 수정, 삭제", description = "생산 대상 정보를 등록, 수정, 삭제합니다. 관리자(ADMIN), 기획자(PLANNER)만 접근할 수 있습니다.")
    @PostMapping("/upsert")
    public ResponseEntity<?> productUpsert(@RequestBody ProductBulkUpsertRequest request,
                                           @RequestAttribute Member member) {
        ProductBulkUpsertResponse response = productService.productBulkUpsertService(member, request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @Operation(summary = "생산 대상 전체 조회", description = "모든 생산 대상 정보를 조회합니다. 키워드로 필터링할 수 있습니다. 관리자(ADMIN), 기획자(PLANNER)만 접근할 수 있습니다.")
    @GetMapping
    public ResponseEntity<?> getAllProducts(@RequestAttribute Member member,
                                            @RequestParam(required = false) String keyword) {
        ProductListResponse response = productService.productListService(member, keyword);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}