
package com.example.cellphoneback.service.operation;

import com.example.cellphoneback.dto.request.operation.TaskBulkUpsertRequest;
import com.example.cellphoneback.dto.response.operation.Task.TaskBulkUpsertResponse;
import com.example.cellphoneback.dto.response.operation.Task.TaskListResponse;
import com.example.cellphoneback.dto.response.operation.Task.TaskParseResponse;
import com.example.cellphoneback.entity.member.Member;
import com.example.cellphoneback.entity.member.Role;
import com.example.cellphoneback.entity.operation.Task;
import com.example.cellphoneback.repository.operation.MachineRepository;
import com.example.cellphoneback.repository.operation.OperationRepository;
import com.example.cellphoneback.repository.operation.TaskRepository;
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
public class TaskService {
    final TaskRepository taskRepository;
    private final MachineRepository machineRepository;
    private final OperationRepository operationRepository;

    // POST	/api/operation/task/xls	기계 엑셀 파싱	admin,planner
    public TaskParseResponse taskParseService(Member member, MultipartFile taskFile){
        if(!member.getRole().equals(Role.ADMIN) && !member.getRole().equals(Role.PLANNER)){
            throw new SecurityException("ADMIN or PLANNER 권한이 없습니다.");
        }
        if(taskFile.isEmpty()){
            throw new NoSuchElementException("파일 내용이 존재하지 않습니다.");
        }

        try {
            Workbook workbook = WorkbookFactory.create(taskFile.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> Iterator = sheet.iterator();
            Row header = Iterator.next();

            DataFormatter formatter = new DataFormatter();
            List<TaskParseResponse.xls> taskXls = new ArrayList<>();
            while(Iterator.hasNext()){
                Row row = Iterator.next();

                TaskParseResponse.xls one =
                        TaskParseResponse.xls.builder()
                                .id(formatter.formatCellValue(row.getCell(0)))
                                .koreanName(formatter.formatCellValue(row.getCell(1)))
                                .operationId(formatter.formatCellValue(row.getCell(2)))
                                .machineId(formatter.formatCellValue(row.getCell(3)))
                                .description(formatter.formatCellValue(row.getCell(4)))
                                .build();
                taskXls.add(one);
            }
            return TaskParseResponse.builder().taskList(taskXls).build();
        } catch (IOException e) {
            throw new RuntimeException("파일 처리 중 오류가 발생했습니다.");
        }
    }

    // POST	/api/operation/task/upsert	기계 등록, 수정, 삭제	admin,planner
    public TaskBulkUpsertResponse taskUpsertResponse(Member member, TaskBulkUpsertRequest request){
        if (!member.getRole().equals(Role.ADMIN) && !member.getRole().equals(Role.PLANNER)) {
            throw new SecurityException("ADMIN or PLANNER 권한이 없습니다.");
        }

        List<TaskBulkUpsertRequest.Item> items = request.getTaskList();
        List<String> taskIds = items.stream().map(e -> e.getId()).toList();

        List<Task> saveTask = taskRepository.findAll();
        List<Task> notContainsTask =
                saveTask.stream()
                        .filter(e -> !taskIds.contains(e.getId())).toList();
        taskRepository.deleteAll(notContainsTask);

        List<Task> upsertTask = items.stream().map(e -> {
            // toEntity() - DTO를 DB에 저장 가능한 Entity로 변환
            // DTO(e)를 Entity로 변환
            // task 자체 필드(id, koreanName, description)만 들어가 있음
            Task task = e.toEntity();
            // DTO가 들고 있는 외래키 값으로 각각 조회하고,
            // 없으면 에러 내보내고 있으면 task에 세팅을 한다.
            task.setOperation(operationRepository.findById(e.getOperationId())
                    .orElseThrow(() -> new NoSuchElementException("operationId 가 없습니다.")));
            task.setMachine(machineRepository.findById(e.getMachineId())
                    .orElseThrow(() -> new NoSuchElementException("machineId 가 없습니다.")));
            // 다 세팅 되었다면 리턴
            return task;
        }).toList();
        taskRepository.saveAll(upsertTask);

        int deleted = notContainsTask.size();
        int updated = saveTask.size() - deleted;
        int created = upsertTask.size() - updated;

        return TaskBulkUpsertResponse.builder()
                .deleteTask(deleted)
                .updateTask(updated)
                .createTask(created).build();

    }




    // GET	/api/operation/task	기계 전체 조회	admin,planner
    public TaskListResponse taskListService(Member member){
        if(!member.getRole().equals(Role.ADMIN) && !member.getRole().equals(Role.PLANNER)){
            throw new SecurityException("ADMIN or PLANNER 권한이 없습니다.");
        }
        List<Task> taskList = taskRepository.findAll();

        return TaskListResponse.builder().taskList(taskList).build();
    }

}
