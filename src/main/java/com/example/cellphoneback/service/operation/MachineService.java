
package com.example.cellphoneback.service.operation;

import com.example.cellphoneback.dto.request.operation.MachineBulkUpsertRequest;
import com.example.cellphoneback.dto.response.operation.machine.MachineBulkUpsertResponse;
import com.example.cellphoneback.dto.response.operation.machine.MachineListResponse;
import com.example.cellphoneback.dto.response.operation.machine.MachineParseResponse;
import com.example.cellphoneback.entity.member.Member;
import com.example.cellphoneback.entity.member.Role;
import com.example.cellphoneback.entity.operation.Machine;
import com.example.cellphoneback.entity.operation.Operation;
import com.example.cellphoneback.repository.operation.MachineRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class MachineService {
    final MachineRepository machineRepository;

    // POST	/api/operation/machine/xls	기계 엑셀 파싱	admin,planner
    public MachineParseResponse machineParseService(Member member, MultipartFile machineFile) {
        if (!member.getRole().equals(Role.ADMIN) && !member.getRole().equals(Role.PLANNER)) {
            throw new SecurityException("ADMIN or PLANNER 권한이 없습니다.");
        }
        if (machineFile.isEmpty()) {
            throw new NoSuchElementException("파일 내용이 존재하지 않습니다.");
        }

        try {
            Workbook workbook = WorkbookFactory.create(machineFile.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = sheet.iterator();
            Row header = iterator.next();

            DataFormatter formatter = new DataFormatter();
            List<MachineParseResponse.xls> machineXls = new ArrayList<>();
            while (iterator.hasNext()) {
                Row row = iterator.next();

                MachineParseResponse.xls one =
                        MachineParseResponse.xls.builder()
                                .id(formatter.formatCellValue(row.getCell(0)))
                                .name(formatter.formatCellValue(row.getCell(1)))
                                .description(formatter.formatCellValue(row.getCell(2)))
                                .build();
                machineXls.add(one);
            }
            return MachineParseResponse.builder().machineList(machineXls).build();
        } catch (IOException e) {
            throw new RuntimeException("파일 처리 중 오류가 발생했습니다.");
        }


    }

    // POST	/api/operation/machine/upsert	기계 등록, 수정, 삭제	admin,planner
    public MachineBulkUpsertResponse machineUpsertService(Member member, MachineBulkUpsertRequest request) {
        if(!member.getRole().equals(Role.ADMIN) &&  !member.getRole().equals(Role.PLANNER)) {
            throw new SecurityException("ADMIN or PLANNER 권한이 없습니다.");
        }

        List<MachineBulkUpsertRequest.Item> items = request.getMachineList();
        List<String> machineIds = items.stream().map(e -> e.getId()).toList();

        List<Machine> saveMachine = machineRepository.findAll();
        List<Machine> notContainsMachine =
                saveMachine.stream()
                        .filter(e -> !machineIds.contains(e.getId())).toList();
        machineRepository.deleteAll(notContainsMachine);

        List<Machine> upsertMachine = items.stream().map(e -> Machine.builder()
                .id(e.getId())
                .name(e.getName())
                .description(e.getDescription())
                .build()).toList();
        machineRepository.saveAll(upsertMachine);

        int deleted = notContainsMachine.size();
        int updated = saveMachine.size() - deleted;
        int created = upsertMachine.size() - updated;

        return MachineBulkUpsertResponse.builder()
                .deleteMachine(deleted)
                .updateMachine(updated)
                .createMachine(created).build();
    }

    // GET	/api/operation/machine	기계 전체 조회	admin,planner
    public MachineListResponse machineListService(Member member, String keyword) {

        if (!member.getRole().equals(Role.ADMIN) && !member.getRole().equals(Role.PLANNER)) {
            throw new SecurityException("ADMIN or PLANNER 권한이 없습니다.");
        }

        List<MachineListResponse.Item> machineList = machineRepository.findAll().stream()
                .map(m -> MachineListResponse.Item.builder()
                        .id(m.getId())
                        .name(m.getName())
                        .description(m.getDescription())
                        .build()).toList();

        // 검색
        List<MachineListResponse.Item> machines = machineList.stream()
                .filter(m -> {
                    if (keyword == null || keyword.isBlank())
                        return true;

                    String k = keyword.trim().toLowerCase();

                    return m.getName() != null && m.getName().contains(k)
                            || m.getDescription() != null && m.getDescription().contains(k);
                })
                .toList();

        return MachineListResponse.builder().machineList(machines).build();
    }
}
