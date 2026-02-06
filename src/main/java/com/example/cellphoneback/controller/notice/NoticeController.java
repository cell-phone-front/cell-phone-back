package com.example.cellphoneback.controller.notice;

import com.example.cellphoneback.dto.request.notice.CreateNoticeRequest;
import com.example.cellphoneback.dto.request.notice.EditNoticeRequest;
import com.example.cellphoneback.dto.response.notice.*;
import com.example.cellphoneback.entity.member.Member;
import com.example.cellphoneback.entity.notice.Notice;
import com.example.cellphoneback.entity.notice.NoticeAttachment;
import com.example.cellphoneback.service.notice.NoticeService;
import org.springframework.core.io.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

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

    // 5	notice	GET	/api/notice/{noticeId}	해당 공지사항 조회	all
    @GetMapping("/{noticeId}")
    public ResponseEntity<SearchNoticeByIdResponse> searchNoticeById(@PathVariable Integer noticeId) {

        SearchNoticeByIdResponse response = noticeService.searchNoticeById(noticeId);

        return ResponseEntity
                .status(HttpStatus.OK) //200
                .body(response);
    }

    // PATCH	/api/notice/{noticeId}/pin	공지사항 핀 고정	admin, planner
    @PatchMapping("/{noticeId}/pin")
    public ResponseEntity<PinNoticeResponse> pinNotice(@RequestAttribute Member member,
                                                       @PathVariable Integer noticeId) {
        PinNoticeResponse response = noticeService.pinNotice(noticeId, member);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    // notice	POST	/api/notice/{noticeId}/attachment	공지사항 파일 첨부	admin, planner	pathvariable = noticeId
    @PostMapping("/{noticeId}/attachment")
    public ResponseEntity<List<NoticeAttachment>> uploadPostFiles(@RequestAttribute Member member,
                                                                  @PathVariable Integer noticeId,
                                                                  @RequestParam("files") List<MultipartFile> files) {

        List<NoticeAttachment> response = noticeService.uploadFiles(member, noticeId, files);

        return ResponseEntity
                .status(HttpStatus.OK) //200
                .body(response);
    }

    // notice	GET	/api/notice/{noticeId}/attachment/{noticeAttachmentId}	공지사항 파일 다운로드	admin, planner
    @GetMapping("/{noticeId}/attachment/{noticeAttachmentId}")
    public ResponseEntity<Resource> getNoticeAttachments(@RequestAttribute Member member,
                                                         @PathVariable Integer noticeId,
                                                         @PathVariable String noticeAttachmentId) throws MalformedURLException {

        Path filePath = noticeService.getNoticeAttachmentPath(member, noticeId, noticeAttachmentId);
        Resource resource = new UrlResource(filePath.toUri());

        String fileName = filePath.getFileName().toString();

        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFileName)
                .body(resource);
    }

    // notice	GET	/api/notice/notification	공지사항 알림 조회	all	pathvariable = memberId
    @GetMapping("/notification")
    public ResponseEntity<List<NoticeNotificationResponse>> getNoticeNotifications(@RequestAttribute Member member) {
        List<NoticeNotificationResponse> response = noticeService.getNoticeNotifications(member);
        return ResponseEntity
                .status(HttpStatus.OK) //200
                .body(response);
    }
}
