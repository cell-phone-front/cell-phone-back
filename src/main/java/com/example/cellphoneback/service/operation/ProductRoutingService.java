package com.example.cellphoneback.service.operation;

import com.example.cellphoneback.dto.request.operation.ProductRoutingBulkUpsertRequest;
import com.example.cellphoneback.dto.response.operation.productRouting.ProductRoutingBulkUpsertResponse;
import com.example.cellphoneback.dto.response.operation.productRouting.ProductRoutingListResponse;
import com.example.cellphoneback.dto.response.operation.productRouting.ProductRoutingParseResponse;
import com.example.cellphoneback.entity.member.Member;
import com.example.cellphoneback.entity.member.Role;
import com.example.cellphoneback.entity.operation.ProductRouting;
import com.example.cellphoneback.repository.operation.OperationRepository;
import com.example.cellphoneback.repository.operation.ProductRepository;
import com.example.cellphoneback.repository.operation.ProductRoutingRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class ProductRoutingService {
    private final ProductRoutingRepository productRoutingRepository;
    private final OperationRepository operationRepository;
    private final ProductRepository  productRepository;

    //    operation	POST	/api/operation/product/routing/xls	프로덕트 라우팅 엑셀 파싱	admin, planner
    public ProductRoutingParseResponse productRoutingParseService(Member member, MultipartFile productRoutingFile) {

        if (!member.getRole().equals(Role.ADMIN) && !member.getRole().equals(Role.PLANNER)) {
            throw new SecurityException("ADMIN, PLANNER 권한이 없습니다.");
        }

        if (productRoutingFile.isEmpty()) {
            throw new NoSuchElementException("파일 내용이 존재하지 않습니다.");
        }

        try {
            // 파일을 Apache POI가 이해할 수 있는 WorkBook으로 변환
            Workbook workbook = WorkbookFactory.create(productRoutingFile.getInputStream());
            // 첫번째 sheet 선택
            Sheet sheet = workbook.getSheetAt(0);
            // 시트에 존재하는 모든 행을 위에서부터 하나씩 읽기 위한 반복자
            Iterator<Row> iterator = sheet.iterator();
            // id,email 등 컬럼이 첫행이라고 보고 분리
            if (!iterator.hasNext()) {
                throw new NoSuchElementException("엑셀에 헤더/데이터가 없습니다.");
            }
            iterator.next();

            DataFormatter formatter = new DataFormatter();
            List<ProductRoutingParseResponse.xls> productRoutingXls = new ArrayList<>();
            while (iterator.hasNext()) {
                Row row = iterator.next();
                // 해당 아이디가 int auto_increment면 엑셀 값을 안 받아도 됨 ( 값이 0으로 들어옴 )
                ProductRoutingParseResponse.xls one =
                        ProductRoutingParseResponse.xls.builder()
                                .id(formatter.formatCellValue(row.getCell(0)))
                                .name(formatter.formatCellValue(row.getCell(1)))
                                .productId(formatter.formatCellValue(row.getCell(2)))
                                .operationId(formatter.formatCellValue(row.getCell(3)))
                                .operationSeq(Integer.parseInt(formatter.formatCellValue(row.getCell(4))))
                                .description(formatter.formatCellValue(row.getCell(5)))
                                .build();
                productRoutingXls.add(one);
            }
            return ProductRoutingParseResponse.builder().productRoutingList(productRoutingXls).build();

        } catch (IOException e) {
            throw new RuntimeException("파일 처리 중 오류가 발생했습니다.");
        }
    }

    //    operation	POST	/api/operation/product/routing/upsert	프로덕트 라우팅  추가, 수정, 삭제	admin, planner
    @Transactional
    public ProductRoutingBulkUpsertResponse productRoutingBulkUpsertService(Member member, ProductRoutingBulkUpsertRequest request) {

        if (!member.getRole().equals(Role.ADMIN) && !member.getRole().equals(Role.PLANNER)) {
            throw new SecurityException("ADMIN, PLANNER 권한이 없습니다.");
        }

        List<ProductRoutingBulkUpsertRequest.Item> rawItem =
                request.getProductRoutingList() == null ? List.of() : request.getProductRoutingList();

        List<ProductRoutingBulkUpsertRequest.Item> items = rawItem;
        // 아디가 String이 아닌 int 이므로 Integer로 받는다.
        List<String> itemIds = items.stream()
                .map(i -> i.getId() == null ? "" : i.getId().trim())
                .toList();

        boolean hasBlankId = itemIds.stream().anyMatch(id -> id.isBlank());
        if (hasBlankId) {
            throw new IllegalArgumentException("ProductRouting id는 필수입니다. 빈 id가 포함되어 있습니다.");
        }

        List<ProductRouting> saveProductRouting = productRoutingRepository.findAll();
        List<ProductRouting> notContainsProductRouting =
                saveProductRouting.stream()
                        .filter(e -> !itemIds.contains(e.getId())).toList();

        int deleted = (int) notContainsProductRouting.stream()
                .filter(r -> Boolean.FALSE.equals(r.getIsDeleted())).count();

        List<ProductRouting> productRoutingDelete = notContainsProductRouting.stream()
                .map(r -> {
                    r.setIsDeleted(true);
                    return r;
                }).toList();
        productRoutingRepository.saveAll(productRoutingDelete);

        List<ProductRouting> UpsertProductRoutingList = items.stream()
                .map(e -> ProductRouting.builder()
                        // 파싱하면서 나온 id 값을 넣어서 저장을하면
                        // 아이디가 자동으로 int auto_increment으로 생성 됨
                        .id(e.getId())
                        .name(e.getName())
                        .product(productRepository.findById(e.getProductId()).orElseThrow())
                        .operation(operationRepository.findById(e.getOperationId()).orElseThrow())
                        .operationSeq(e.getOperationSeq())
                        .description(e.getDescription())
                        .isDeleted(false )
                        .build())
                .toList();
        productRoutingRepository.saveAll(UpsertProductRoutingList);


        int updated = 0;
        int created = 0;

        for (String id : itemIds) {
            boolean exists = false;
            for (ProductRouting r : saveProductRouting) {
                if (r.getId().equals(id)) {
                    exists = true;
                    break;
                }
            }
            if (exists) updated++;
            else created++;
        }

        return ProductRoutingBulkUpsertResponse.builder()
                .createProductRouting(created)
                .deleteProductRouting(deleted)
                .updateProductRouting(updated).build();
    }

    //    operation	GET	/api/operation/product/routing	프로덕트 라우팅  전체 조회	admin, planner
    public ProductRoutingListResponse productRoutingListService(Member member, String keyword) {

        if (!member.getRole().equals(Role.ADMIN) && !member.getRole().equals(Role.PLANNER)) {
            throw new SecurityException("ADMIN, PLANNER 권한이 없습니다.");
        }

        List<ProductRouting> alive = productRoutingRepository.findAll().stream()
                .filter(p -> Boolean.FALSE.equals(p.getIsDeleted()))
                .toList();

        List<ProductRoutingListResponse.Item> productRoutingList = alive.stream()
                .map(r -> ProductRoutingListResponse.Item.builder()
                        .id(r.getId())
                        .name(r.getName())
                        .productId(r.getProduct().getId())
                        .operationId(r.getOperation().getId())
                        .operationSeq(r.getOperationSeq())
                        .description(r.getDescription())
                        .build()).toList();

        List<ProductRoutingListResponse.Item> productRouting = productRoutingList.stream()
                .filter(r -> {
                    if(keyword == null || keyword.isBlank()) {
                        return true;
                    }

                    String k = keyword.trim().toLowerCase().replaceAll("\\s+", "");

                    return r.getName() != null && r.getName().toLowerCase().replaceAll("\\s+", "").contains(k)
                            || r.getDescription() != null && r.getDescription().toLowerCase().replaceAll("\\s+", "").contains(k);
                })
                .sorted((a, b) -> {
                    String ap = a.getProductId();
                    String bp = b.getProductId();

                    // null-safe 문자열 비교 (null은 뒤로)
                    if (ap == null && bp != null) return 1;
                    if (ap != null && bp == null) return -1;
                    if (ap != null) {
                        int c = ap.compareTo(bp);
                        if (c != 0) return c;
                    }

                    // operationSeq 오름차순
                    int s = Integer.compare(a.getOperationSeq(), b.getOperationSeq());
                    if (s != 0) return s;

                    // (선택) id 오름차순으로 안정 정렬
                    String ai = a.getId();
                    String bi = b.getId();
                    if (ai == null && bi != null) return 1;
                    if (ai != null && bi == null) return -1;
                    if (ai == null) return 0;
                    return ai.compareTo(bi);
                }).toList();



        return ProductRoutingListResponse.builder().productRoutingList(productRouting).build();
    }
}