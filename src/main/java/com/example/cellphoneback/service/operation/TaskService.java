
package com.example.cellphoneback.service.operation;

import com.example.cellphoneback.dto.request.operation.TaskBulkUpsertRequest;
import com.example.cellphoneback.dto.response.operation.task.TaskBulkUpsertResponse;
import com.example.cellphoneback.dto.response.operation.task.TaskListResponse;
import com.example.cellphoneback.dto.response.operation.task.TaskParseResponse;
import com.example.cellphoneback.entity.member.Member;
import com.example.cellphoneback.entity.member.Role;
import com.example.cellphoneback.entity.operation.Operation;
import com.example.cellphoneback.entity.operation.Task;
import com.example.cellphoneback.repository.operation.MachineRepository;
import com.example.cellphoneback.repository.operation.OperationRepository;
import com.example.cellphoneback.repository.operation.TaskRepository;
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
public class TaskService {
    final TaskRepository taskRepository;
    private final MachineRepository machineRepository;
    private final OperationRepository operationRepository;

    // POST	/api/operation/task/xls	작업 엑셀 파싱	admin,planner
    public TaskParseResponse taskParseService(Member member, MultipartFile taskFile) {
        if (!member.getRole().equals(Role.ADMIN) && !member.getRole().equals(Role.PLANNER)) {
            throw new SecurityException("ADMIN, PLANNER 권한이 없습니다.");
        }
        if (taskFile.isEmpty()) {
            throw new NoSuchElementException("파일 내용이 존재하지 않습니다.");
        }

        try {
            Workbook workbook = WorkbookFactory.create(taskFile.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> Iterator = sheet.iterator();

            if (!Iterator.hasNext()) {
                throw new NoSuchElementException("엑셀에 헤더/데이터가 없습니다.");
            }
            Iterator.next();

            DataFormatter formatter = new DataFormatter();
            List<TaskParseResponse.xls> taskXls = new ArrayList<>();
            while (Iterator.hasNext()) {
                Row row = Iterator.next();
                // duration이 60이 아니라 60.0 이거나 공백을 포함하고 있으면 터질 수 있기 때문에
                // 안전하게 받을 수 있는 작업을 함
                String durationRaw = formatter.formatCellValue(row.getCell(4));
                int duration = 0;

                if (durationRaw != null) {
                    durationRaw = durationRaw.trim();
                    if (!durationRaw.isBlank()) {
                        try {
                            // "60.0" 같은 값 방어
                            if (durationRaw.contains(".")) {
                                duration = (int) Double.parseDouble(durationRaw);
                            } else {
                                duration = Integer.parseInt(durationRaw);
                            }
                        } catch (NumberFormatException e) {
                            throw new IllegalArgumentException("duration 값이 숫자가 아닙니다: " + durationRaw);
                        }
                    }
                }

                TaskParseResponse.xls one =
                        TaskParseResponse.xls.builder()
                                .id(formatter.formatCellValue(row.getCell(0)))
                                .operationId(formatter.formatCellValue(row.getCell(1)))
                                .machineId(formatter.formatCellValue(row.getCell(2)))
                                .name(formatter.formatCellValue(row.getCell(3)))
                                .duration(duration)
                                .description(formatter.formatCellValue(row.getCell(5)))
                                .build();
                taskXls.add(one);
            }
            return TaskParseResponse.builder().taskList(taskXls).build();
        } catch (IOException e) {
            throw new RuntimeException("파일 처리 중 오류가 발생했습니다.");
        }
    }

    // POST	/api/operation/task/upsert	작업 등록, 수정, 삭제	admin,planner
    @Transactional
    public TaskBulkUpsertResponse taskUpsertResponse(Member member, TaskBulkUpsertRequest request) {
        if (!member.getRole().equals(Role.ADMIN) && !member.getRole().equals(Role.PLANNER)) {
            throw new SecurityException("ADMIN, PLANNER 권한이 없습니다.");
        }

        List<TaskBulkUpsertRequest.Item> items = request.getTaskList();
        List<String> taskIds = items.stream()
                .map(e -> e.getId() == null ? "" : e.getId().trim())
                .toList();

        boolean hasBlankId = taskIds.stream().anyMatch(id -> id.isBlank());
        if (hasBlankId) {
            throw new IllegalArgumentException("Task id는 필수입니다. 빈 id가 포함되어 있습니다.");
        }

        List<Task> saveTask = taskRepository.findAll();
        List<Task> notContainsTask =
                saveTask.stream()
                        .filter(e -> !taskIds.contains(e.getId())).toList();

        int deleted = (int) notContainsTask.stream()
                .filter(t -> Boolean.FALSE.equals(t.getIsDeleted())).count();

        List<Task> taskDeleted = notContainsTask.stream()
                .map(t -> {
                    t.setIsDeleted(true);
                    return t;
                }).toList();
        taskRepository.saveAll(taskDeleted);

        List<Task> upsertTask = items.stream().map(e -> {
            // DTO -> Entity로 변환
            Task task = e.toEntity();

            // DTO가 들고 있는 외래키 값으로 각각 조회하고,
            // 없으면 에러 내보내고 있으면 task에 세팅을 한다.
            task.setOperation(operationRepository.findById(e.getOperationId())
                    .orElseThrow(() -> new NoSuchElementException("operationId 가 없습니다.")));
            task.setMachine(machineRepository.findById(e.getMachineId())
                    .orElseThrow(() -> new NoSuchElementException("machineId 가 없습니다.")));
            task.setIsDeleted(false);
            // 다 세팅 되었다면 리턴
            return task;
        }).toList();
        taskRepository.saveAll(upsertTask);

        int updated = 0;
        int created = 0;

        for (String id : taskIds) {
            boolean exists = false;
            for (Task t : saveTask) {
                if (t.getId().equals(id)) {
                    exists = true;
                    break;
                }
            }
            if (exists) updated++;
            else created++;
        }

        return TaskBulkUpsertResponse.builder()
                .deleteTask(deleted)
                .updateTask(updated)
                .createTask(created).build();

    }


    // GET	/api/operation/task	작업 전체 조회	admin,planner
    public TaskListResponse taskListService(Member member, String keyword) {
        if (!member.getRole().equals(Role.ADMIN) && !member.getRole().equals(Role.PLANNER)) {
            throw new SecurityException("ADMIN, PLANNER 권한이 없습니다.");
        }
        List<TaskListResponse.Item> taskList = taskRepository.findAll().stream()
                .filter(t -> Boolean.FALSE.equals(t.getIsDeleted()))
                .map(t -> TaskListResponse.Item.builder()
                        .id(t.getId())
                        .operationId(t.getOperation().getId())
                        .machineId(t.getMachine().getId())
                        .name(t.getName())
                        .duration(t.getDuration())
                        .description(t.getDescription()).build()).toList();

        // 검색
        List<TaskListResponse.Item> tasks = taskList.stream()
                .filter(t -> {
                    if (keyword == null || keyword.isBlank())
                        return true;

                    String k = keyword.trim().toLowerCase().replaceAll("\\s+", "");

                    return t.getName() != null && t.getName().toLowerCase().replaceAll("\\s+", "").contains(k)
                            || t.getDescription() != null && t.getDescription().toLowerCase().replaceAll("\\s+", "").contains(k);
                })
                .toList();

        return TaskListResponse.builder().taskList(tasks).build();
    }

}
