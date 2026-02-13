
package com.example.cellphoneback.service.operation;

import com.example.cellphoneback.dto.request.operation.ProductBulkUpsertRequest;
import com.example.cellphoneback.dto.response.operation.product.ProductBulkUpsertResponse;
import com.example.cellphoneback.dto.response.operation.product.ProductListResponse;
import com.example.cellphoneback.dto.response.operation.product.ProductParseResponse;
import com.example.cellphoneback.entity.member.Member;
import com.example.cellphoneback.entity.member.Role;
import com.example.cellphoneback.entity.operation.Operation;
import com.example.cellphoneback.entity.operation.Product;
import com.example.cellphoneback.repository.operation.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;

    //    operation	POST	/api/operation/product/xls	생산 대상 엑셀 파싱	admin, planner
    public ProductParseResponse productParseService(Member member, MultipartFile productFile) {

        if (!member.getRole().equals(Role.ADMIN) && !member.getRole().equals(Role.PLANNER)) {
            throw new SecurityException("ADMIN, PLANNER 권한이 없습니다.");
        }

        if (productFile.isEmpty()) {
            throw new NoSuchElementException("파일 내용이 존재하지 않습니다.");
        }

        try {
            // 파일을 Apache POI가 이해할 수 있는 WorkBook으로 변환
            Workbook workbook = WorkbookFactory.create(productFile.getInputStream());
            // 첫번째 sheet 선택
            Sheet sheet = workbook.getSheetAt(0);
            // 시트에 존재하는 모든 행을 위에서부터 하나씩 읽기 위한 반복자
            Iterator<Row> iterator = sheet.iterator();
            // id,email 등 컬럼이 첫행이라고 보고 분리
            Row header = iterator.next();

            DataFormatter formatter = new DataFormatter();
            List<ProductParseResponse.xls> productXls = new ArrayList<>();
            while (iterator.hasNext()) {
                Row row = iterator.next();
                // 해당 아이디가 int auto_increment면 엑셀 값을 안 받아도 됨 ( 값이 0으로 들어옴 )
                ProductParseResponse.xls one =
                        ProductParseResponse.xls.builder()
                                .id(formatter.formatCellValue(row.getCell(0)))
                                .brand(formatter.formatCellValue(row.getCell(1)))
                                .name(formatter.formatCellValue(row.getCell(2)))
                                .description(formatter.formatCellValue(row.getCell(3)))
                                .build();
                productXls.add(one);
            }
            return ProductParseResponse.builder().productList(productXls).build();

        } catch (IOException e) {
            throw new RuntimeException("파일 처리 중 오류가 발생했습니다.");
        }
    }

    //    operation	POST	/api/operation/product/upsert	생산 대상 등록,  수정, 삭제	admin, planner
    public ProductBulkUpsertResponse productBulkUpsertService(Member member, ProductBulkUpsertRequest request) {

        if (!member.getRole().equals(Role.ADMIN) && !member.getRole().equals(Role.PLANNER)) {
            throw new SecurityException("ADMIN, PLANNER 권한이 없습니다.");
        }
        List<ProductBulkUpsertRequest.Item> items = request.getProductList();
        // 아디가 String이 아닌 int 이므로 Integer로 받는다.
        List<String> itemIds = items.stream().map(e -> e.getId()).toList();

        List<Product> saveProduct = productRepository.findAll();
        List<Product> notContainsProduct =
                saveProduct.stream()
                        .filter(e -> !itemIds.contains(e.getId())).toList();
        productRepository.deleteAll(notContainsProduct);

        List<Product> UpsertProductList = items.stream()
                .map(e -> Product.builder()
                        // 파싱하면서 나온 id 값을 넣어서 저장을하면
                        // 아이디가 자동으로 int auto_increment으로 생성 됨
                        .id(e.getId())
                        .brand(e.getBrand())
                        .name(e.getName())
                        .description(e.getDescription())
                        .build()).toList();
        productRepository.saveAll(UpsertProductList);

        int deleted = notContainsProduct.size();
        int updated = saveProduct.size() - deleted;
        int created = UpsertProductList.size() - updated;

        return ProductBulkUpsertResponse.builder()
                .createProduct(created)
                .deleteProduct(deleted)
                .updateProduct(updated).build();
    }

    // 	operation	GET	/api/operation/product	생산 대상 전체 조회 및 검색 조회	admin, planner
    public ProductListResponse productListService(Member member, String keyword) {

        if (!member.getRole().equals(Role.ADMIN) && !member.getRole().equals(Role.PLANNER)) {
            throw new SecurityException("ADMIN, PLANNER 권한이 없습니다.");
        }

        List<ProductListResponse.Item> productList = productRepository.findAll().stream()
                .map(p -> ProductListResponse.Item.builder()
                        .id(p.getId())
                        .brand(p.getBrand())
                        .name(p.getName())
                        .description(p.getDescription())
                        .build()).toList();

        // 검색
        List<ProductListResponse.Item> products = productList.stream()
                .filter(p -> {
                    if (keyword == null || keyword.isBlank())
                        return true;

                    String k = keyword.trim().toLowerCase().replaceAll("\\s+", "");

                    return p.getName() != null && p.getName().toLowerCase().replaceAll("\\s+", "").contains(k)
                            || p.getDescription() != null && p.getDescription().toLowerCase().replaceAll("\\s+", "").contains(k);
                })
                .toList();

        return ProductListResponse.builder().productList(products).build();
    }
}
