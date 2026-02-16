
package com.example.cellphoneback.service.operation;

import com.example.cellphoneback.dto.request.operation.OperationBulkUpsertRequest;
import com.example.cellphoneback.dto.response.community.SearchCommunityByIdResponse;
import com.example.cellphoneback.dto.response.operation.operation.OperationBulkUpsertResponse;
import com.example.cellphoneback.dto.response.operation.operation.OperationListResponse;
import com.example.cellphoneback.dto.response.operation.operation.OperationParseResponse;
import com.example.cellphoneback.entity.community.Community;
import com.example.cellphoneback.entity.member.Member;
import com.example.cellphoneback.entity.member.Role;
import com.example.cellphoneback.entity.operation.Operation;
import com.example.cellphoneback.repository.operation.OperationRepository;
import com.example.cellphoneback.repository.operation.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RequiredArgsConstructor
@Service
public class OperationService {
    private final OperationRepository operationRepository;
    private final ProductRepository productRepository;

    //    operation	POST	/api/operation/xls	공정 단계 엑셀 파싱	admin, planner
    public OperationParseResponse operationParseService(Member member, MultipartFile operationFile) {

        if (!member.getRole().equals(Role.ADMIN) && !member.getRole().equals(Role.PLANNER)) {
            throw new SecurityException("ADMIN, PLANNER 권한이 없습니다.");
        }

        if (operationFile.isEmpty()) {
            throw new NoSuchElementException("파일 내용이 존재하지 않습니다.");
        }

        try {
            // 파일을 Apache POI가 이해할 수 있는 WorkBook으로 변환
            Workbook workbook = WorkbookFactory.create(operationFile.getInputStream());
            // 첫번째 sheet 선택
            Sheet sheet = workbook.getSheetAt(0);
            // 시트에 존재하는 모든 행을 위에서부터 하나씩 읽기 위한 반복자
            Iterator<Row> iterator = sheet.iterator();
            // id,email 등 컬럼이 첫행이라고 보고 분리
            if (!iterator.hasNext()) {
                throw new NoSuchElementException("엑셀에 헤더가 없습니다.");
            }
            iterator.next();

            DataFormatter formatter = new DataFormatter();
            List<OperationParseResponse.xls> operationXls = new ArrayList<>();
            while (iterator.hasNext()) {
                Row row = iterator.next();

                OperationParseResponse.xls one =
                        OperationParseResponse.xls.builder()
                                .id(formatter.formatCellValue(row.getCell(0)))
                                .name(formatter.formatCellValue(row.getCell(1)))
                                .description(formatter.formatCellValue(row.getCell(2)))
                                .build();
                operationXls.add(one);
            }
            return OperationParseResponse.builder().operationList(operationXls).build();

        } catch (IOException e) {
            throw new RuntimeException("파일 처리 중 오류가 발생했습니다.");
        }
    }

    //    operation	POST	/api/operation/upsert	공정 단계 추가, 수정, 삭제	admin, planner
    @Transactional
    public OperationBulkUpsertResponse operationBulkUpsertService(Member member, OperationBulkUpsertRequest request) {

        if (!member.getRole().equals(Role.ADMIN) && !member.getRole().equals(Role.PLANNER)) {
            throw new SecurityException("ADMIN, PLANNER 권한이 없습니다.");
        }

        List<OperationBulkUpsertRequest.Item> rawItem =
                request.getOperationList() == null ? List.of() : request.getOperationList();

        List<OperationBulkUpsertRequest.Item> items = rawItem;
        List<String> itemIds = items.stream()
                .map(i -> i.getId() == null ? "" : i.getId().trim())
                .toList();

        boolean hasBlankId = itemIds.stream().anyMatch(id -> id == null || id.isBlank());
        if (hasBlankId) {
            throw new IllegalArgumentException("operationList에 비어있는 id가 있습니다."); // 400
        }

        List<Operation> saveOperation = operationRepository.findAll();
        List<Operation> notContainsOperation =
                saveOperation.stream()
                        .filter(e -> !itemIds.contains(e.getId())).toList();

        int deleted = (int) notContainsOperation.stream()
                .filter(op -> Boolean.FALSE.equals(op.getIsDeleted())).count();

        List<Operation> deleteOperation = notContainsOperation.stream()
                .map(op -> {
                    op.setIsDeleted(true);
                    return op;
                }).toList();
        operationRepository.saveAll(deleteOperation);

        List<Operation> UpsertOperations = items.stream()
                .map(e -> Operation.builder()
                        .id(e.getId())
                        .name(e.getName())
                        .description(e.getDescription())
                        .isDeleted(false)
                        .build()).toList();
        operationRepository.saveAll(UpsertOperations);

        int updated = 0;
        int created = 0;

        for (String id : itemIds) {
            boolean exists = false;
            for (Operation o : saveOperation) {
                if (o.getId().equals(id)) {
                    exists = true;
                    break;
                }
            }
            if (exists) updated++;
            else created++;
        }

        return OperationBulkUpsertResponse.builder()
                .createOperation(created)
                .deleteOperation(deleted)
                .updateOperation(updated).build();
    }

    // 	operation	GET	/api/operation	공정 단계 전체 조회	admin, planner
    public OperationListResponse operationListService(Member member, String keyword) {

        if (!member.getRole().equals(Role.ADMIN) && !member.getRole().equals(Role.PLANNER)) {
            throw new SecurityException("ADMIN, PLANNER 권한이 없습니다.");
        }

        List<OperationListResponse.Item> operationList = operationRepository.findAll().stream()
                .filter(o -> Boolean.FALSE.equals(o.getIsDeleted()))
                .map(o -> OperationListResponse.Item.builder()
                        .id(o.getId())
                        .name(o.getName())
                        .description(o.getDescription())
                        .build()).toList();

        // 검색
        List<OperationListResponse.Item> operations = operationList.stream()
                .filter(o -> {
                    if (keyword == null || keyword.isBlank())
                        return true;

                    String k = keyword.trim().toLowerCase().replaceAll("\\s+", "");

                    return o.getName() != null && o.getName().toLowerCase().replaceAll("\\s+", "").contains(k)
                            || o.getDescription() != null && o.getDescription().toLowerCase().replaceAll("\\s+", "").contains(k);
                })
                .toList();

        return OperationListResponse.builder().operationList(operations).build();
    }
}
