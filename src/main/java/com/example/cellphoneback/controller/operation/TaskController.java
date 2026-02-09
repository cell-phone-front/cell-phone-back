package com.example.cellphoneback.controller.operation;

import com.example.cellphoneback.dto.request.operation.TaskBulkUpsertRequest;
import com.example.cellphoneback.dto.response.operation.task.TaskBulkUpsertResponse;
import com.example.cellphoneback.dto.response.operation.task.TaskListResponse;
import com.example.cellphoneback.dto.response.operation.task.TaskParseResponse;
import com.example.cellphoneback.entity.member.Member;
import com.example.cellphoneback.service.operation.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Task", description = "작업 관련 API")
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/operation/task")
public class TaskController {
    final TaskService taskService;

    @Operation(summary = "작업 엑셀 파싱", description = "작업 엑셀 파일을 파싱합니다.")
    @PostMapping("/xls")
    public ResponseEntity<TaskParseResponse> taskParse(@RequestAttribute Member member,
                                                       @RequestBody MultipartFile file) {
        TaskParseResponse response = taskService.taskParseService(member, file);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "작업 등록, 수정, 삭제", description = "작업을 등록, 수정, 삭제합니다.")
    @PostMapping("/upsert")
    public ResponseEntity<TaskBulkUpsertResponse> taskUpsert(@RequestAttribute Member member,
                                                             @RequestBody TaskBulkUpsertRequest request) {
        TaskBulkUpsertResponse response = taskService.taskUpsertResponse(member, request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "작업 전체 조회", description = "작업을 전체 조회합니다. 키워드로 필터링할 수 있습니다.")
    @GetMapping
    public ResponseEntity<TaskListResponse> taskList(@RequestAttribute Member member,
                                                     @RequestParam(required = false) String keyword) {

        TaskListResponse response = taskService.taskListService(member, keyword);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
