package com.example.cellphoneback.controller.operation;

import com.example.cellphoneback.dto.request.operation.ProductBulkUpsertRequest;
import com.example.cellphoneback.dto.response.operation.product.ProductBulkUpsertResponse;
import com.example.cellphoneback.dto.response.operation.product.ProductListResponse;
import com.example.cellphoneback.dto.response.operation.product.ProductParseResponse;
import com.example.cellphoneback.entity.member.Member;
import com.example.cellphoneback.service.operation.ProductService;
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
@RequestMapping("/api/operation/product")
public class ProductController {
    private final ProductService productService;

//    operation	POST	/api/operation/product/xls	생산 대상 엑셀 파싱	admin, planner
    @PostMapping("/xls")
    public ResponseEntity<?> productXls(@RequestBody MultipartFile file,
                                        @RequestAttribute Member member) {

       ProductParseResponse response = productService.productParseService(member, file);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
//    operation	POST	/api/operation/product/upsert	생산 대상 등록,  수정, 삭제	admin, planner
    @PostMapping("/upsert")
    public ResponseEntity<?> productUpsert(@RequestBody ProductBulkUpsertRequest request,
                                           @RequestAttribute Member member) {
        ProductBulkUpsertResponse response = productService.productBulkUpsertService(member, request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
// 	operation	GET	/api/operation/product	생산 대상 전체 조회	admin, planner
    @GetMapping
    public ResponseEntity<?> getAllProducts(@RequestAttribute Member member,
                                            @RequestParam(required = false) String keyword) {
        ProductListResponse response = productService.productListService(member, keyword);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}