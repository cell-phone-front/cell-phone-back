package com.example.cellphoneback.controller.operation;

import com.example.cellphoneback.dto.request.operation.TaskBulkUpsertRequest;
import com.example.cellphoneback.dto.response.operation.task.TaskBulkUpsertResponse;
import com.example.cellphoneback.dto.response.operation.task.TaskListResponse;
import com.example.cellphoneback.dto.response.operation.task.TaskParseResponse;
import com.example.cellphoneback.entity.member.Member;
import com.example.cellphoneback.service.operation.TaskService;
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
@RequestMapping("/api/operation/task")
public class TaskController {
    final TaskService taskService;

    // POST	/api/operation/task/xls	기계 엑셀 파싱	admin,planner
    @PostMapping("/xls")
    public ResponseEntity<TaskParseResponse> taskParse(@RequestAttribute Member member,
                                                       @RequestBody MultipartFile file) {
        TaskParseResponse response = taskService.taskParseService(member, file);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // POST	/api/operation/task/upsert	기계 등록, 수정, 삭제	admin,planner
    @PostMapping("/upsert")
    public ResponseEntity<TaskBulkUpsertResponse> taskUpsert(@RequestAttribute Member member,
                                                             @RequestBody TaskBulkUpsertRequest request) {
        TaskBulkUpsertResponse response = taskService.taskUpsertResponse(member, request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // GET	/api/operation/task	기계 전체 조회	admin,planner
    @GetMapping
    public ResponseEntity<TaskListResponse> taskList(@RequestAttribute Member member,
                                                     @RequestBody( required = false) String keyword) {

        TaskListResponse response = taskService.taskListService(member, keyword);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
