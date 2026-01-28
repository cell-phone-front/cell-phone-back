
package com.example.cellphoneback.service.operation;

import com.example.cellphoneback.dto.request.operation.OperationBulkUpsertRequest;
import com.example.cellphoneback.dto.response.operation.Operation.OperationBulkUpsertResponse;
import com.example.cellphoneback.dto.response.operation.Operation.OperationListResponse;
import com.example.cellphoneback.dto.response.operation.Operation.OperationParseResponse;
import com.example.cellphoneback.entity.member.Member;
import com.example.cellphoneback.entity.member.Role;
import com.example.cellphoneback.entity.operation.Operation;
import com.example.cellphoneback.repository.operation.OperationRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RequiredArgsConstructor
@Service
public class OperationService {
    private final OperationRepository operationRepository;

//    operation	POST	/api/operation/xls	공정 단계 엑셀 파싱	admin, planner
public OperationParseResponse operationParseService(Member member, MultipartFile operationFile){

    if (!member.getRole().equals(Role.ADMIN) && !member.getRole().equals(Role.PLANNER)) {
        throw new SecurityException("공정 단계 엑셀 파싱 권한이 없습니다.");
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
        Row header = iterator.next();

        DataFormatter formatter = new DataFormatter();
        List<OperationParseResponse.xls> operationXls = new ArrayList<>();
        while (iterator.hasNext()) {
            Row row = iterator.next();

            OperationParseResponse.xls one =
                    OperationParseResponse.xls.builder()
                            .id(formatter.formatCellValue(row.getCell(0)))
                            .koreanName(formatter.formatCellValue(row.getCell(1)))
                            .productId(formatter.formatCellValue(row.getCell(2)))
                            .description(formatter.formatCellValue(row.getCell(3)))
                            .duration(Double.parseDouble(formatter.formatCellValue(row.getCell(4))))
                            .build();
            operationXls.add(one);
        }
        return OperationParseResponse.builder().operationList(operationXls).build();

    } catch (IOException e) {
        throw new RuntimeException("파일 처리 중 오류가 발생했습니다.");
    }
}

//    operation	POST	/api/operation/upsert	공정 단계 추가, 수정, 삭제	admin, planner
public OperationBulkUpsertResponse operationBulkUpsertService(Member member, OperationBulkUpsertRequest request) {

    if (!member.getRole().equals(Role.ADMIN) && !member.getRole().equals(Role.PLANNER)) {
        throw new SecurityException("공정 단계 수정 권한이 없습니다.");
    }

    List<OperationBulkUpsertRequest.Item> items = request.getOperationList();
    List<String> itemIds = items.stream().map(e -> e.getId()).toList();

    List<Operation> saveOperation = operationRepository.findAll();
    List<Operation> notContainsOperation =
            saveOperation.stream()
                    .filter(e -> !itemIds.contains(e.getId())).toList();
    operationRepository.deleteAll(notContainsOperation);

    List<Operation> UpsertOperations = items.stream().map(e -> Operation.builder()
            .id(e.getId())
            .koreanName(e.getKoreanName())
            .product(e.getProduct())
            .description(e.getDescription())
            .duration(e.getDuration())
            .build()).toList();
    operationRepository.saveAll(UpsertOperations);

    int deleted = notContainsOperation.size();
    int updated = saveOperation.size() - deleted;
    int created = UpsertOperations.size() - updated;

    return OperationBulkUpsertResponse.builder()
            .createOperation(created)
            .deleteOperation(deleted)
            .updateOperation(updated).build();
}

    // 	operation	GET	/api/operation	공정 단계 전체 조회	admin, planner
    public OperationListResponse operationListService(Member member) {

        if (!member.getRole().equals(Role.ADMIN) && !member.getRole().equals(Role.PLANNER)) {
            throw new SecurityException("공정 단계 조회 권한이 없습니다.");
        }

        List<Operation> operationList = operationRepository.findAll();
        return OperationListResponse.builder().operationList(operationList).build();
    }
}
