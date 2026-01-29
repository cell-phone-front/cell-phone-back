package com.example.cellphoneback.controller.operation;

import com.example.cellphoneback.dto.request.operation.ProductBulkUpsertRequest;
import com.example.cellphoneback.dto.request.operation.ProductRoutingBulkUpsertRequest;
import com.example.cellphoneback.dto.response.operation.product.ProductBulkUpsertResponse;
import com.example.cellphoneback.dto.response.operation.product.ProductListResponse;
import com.example.cellphoneback.dto.response.operation.product.ProductParseResponse;
import com.example.cellphoneback.dto.response.operation.productRouting.ProductRoutingBulkUpsertResponse;
import com.example.cellphoneback.dto.response.operation.productRouting.ProductRoutingListResponse;
import com.example.cellphoneback.dto.response.operation.productRouting.ProductRoutingParseResponse;
import com.example.cellphoneback.entity.member.Member;
import com.example.cellphoneback.service.operation.ProductRoutingService;
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
@RequestMapping("/api/operation/product-routing")
public class ProductRoutingController {
    private final ProductRoutingService productRoutingService;

    //    operation	POST	/api/operation/product-routing/xls	프로덕트 라우팅 엑셀 파싱	admin, planner
    @PostMapping("/xls")
    public ResponseEntity<?> productRoutingXls(@RequestBody MultipartFile file,
                                               @RequestAttribute Member member) {

        ProductRoutingParseResponse response = productRoutingService.productRoutingParseService(member, file);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //    operation	POST	/api/operation/product-routing/upsert	프로덕트 라우팅  추가, 수정, 삭제	admin, planner
    @PostMapping("/upsert")
    public ResponseEntity<?> productRoutingUpsert(@RequestBody ProductRoutingBulkUpsertRequest request,
                                                  @RequestAttribute Member member) {
        ProductRoutingBulkUpsertResponse response = productRoutingService.productRoutingBulkUpsertService(member, request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //    operation	GET	/api/operation/product-routing	프로덕트 라우팅  전체 조회	admin, planner
    @GetMapping
    public ResponseEntity<?> getAllProductRouting(@RequestAttribute Member member) {
        ProductRoutingListResponse response = productRoutingService.productRoutingListService(member);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
