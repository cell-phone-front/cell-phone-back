package com.example.cellphoneback.controller.notice;

import com.example.cellphoneback.dto.request.notice.CreateNoticeRequest;
import com.example.cellphoneback.dto.request.notice.EditNoticeRequest;
import com.example.cellphoneback.dto.response.notice.*;
import com.example.cellphoneback.entity.member.Member;
import com.example.cellphoneback.entity.notice.Notice;
import com.example.cellphoneback.service.notice.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

//@SecurityRequirement(name = "bearerAuth")
//@Tag(name = "Comment", description = "댓글 관련 API")
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/notice")
public class NoticeController {
    private final NoticeService noticeService;

    //1	notice	POST	/api/notice	공지사항 작성	admin, planner
    @PostMapping
    public ResponseEntity<CreateNoticeResponse> createNotice(@RequestAttribute Member member,
                                                             @RequestBody CreateNoticeRequest request) {

        Notice response = noticeService.createNotice(member, request);

        return ResponseEntity
                .status(HttpStatus.CREATED) //201
                .body(CreateNoticeResponse.fromEntity(response));
    }

    //2	notice	PUT	/api/notice	공지사항 수정	admin, planner
    @PutMapping("/{noticeId}")
    public ResponseEntity<EditNoticeResponse> updateNotice(@RequestAttribute Member member,
                                                           @PathVariable Integer noticeId,
                                                           @RequestBody EditNoticeRequest request) {
        Notice response = noticeService.editNotice(noticeId, member, request);
        return ResponseEntity
                .status(HttpStatus.OK) //201
                .body(EditNoticeResponse.fromEntity(response));
    }

    //3	notice	DELETE	/api/notice	공지사항 삭제	admin, planner
    @DeleteMapping("/{noticeId}")
    public ResponseEntity<DeleteNoticeResponse> deleteNotice(@RequestAttribute Member member,
                                                             @PathVariable Integer noticeId) {
        noticeService.deleteNotice(member, noticeId);

        return ResponseEntity
                .status(HttpStatus.OK) //201
                .body(DeleteNoticeResponse.fromEntity());
    }

    //4	notice	GET	/api/notice	공지사항 조회	all
    @GetMapping
    public ResponseEntity<SearchAllNoticeResponse> getNotice(@RequestAttribute Member member,
                                                                   @RequestParam(required = false) String keyword) {
        SearchAllNoticeResponse response = noticeService.searchAllNotice(member, keyword);
        
        return ResponseEntity
                .status(HttpStatus.OK) //200
                .body(response);
    }

    // 5	notice	GET	/api/notice/{noticeId}	해당 공지사항 조회	all	pathvariable = noticeId
    @GetMapping("/{noticeId}")
    public ResponseEntity<SearchNoticeByIdResponse> searchNoticeById(@RequestParam Integer noticeId) {

        Notice response = noticeService.searchNoticeById(noticeId);

        return ResponseEntity
                .status(HttpStatus.OK) //201
                .body(SearchNoticeByIdResponse.fromEntity(response));
    }
}
