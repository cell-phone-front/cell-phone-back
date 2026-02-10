package com.example.cellphoneback.controller.notice;

import com.example.cellphoneback.dto.request.notice.CreateNoticeRequest;
import com.example.cellphoneback.dto.request.notice.EditNoticeRequest;
import com.example.cellphoneback.dto.response.notice.*;
import com.example.cellphoneback.entity.member.Member;
import com.example.cellphoneback.entity.notice.Notice;
import com.example.cellphoneback.entity.notice.NoticeAttachment;
import com.example.cellphoneback.service.notice.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Notice", description = "공지사항 관련 API")
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/notice")
public class NoticeController {
    private final NoticeService noticeService;

    @Operation(summary = "공지사항 작성", description = "새 공지사항을 작성합니다.")
    @PostMapping
    public ResponseEntity<CreateNoticeResponse> createNotice(@RequestAttribute Member member,
                                                             @RequestBody CreateNoticeRequest request) {

        Notice response = noticeService.createNotice(member, request);

        return ResponseEntity
                .status(HttpStatus.CREATED) //201
                .body(CreateNoticeResponse.fromEntity(response));
    }

    @Operation(summary = "공지사항 수정", description = "기존 공지사항을 수정합니다.")
    @PutMapping("/{noticeId}")
    public ResponseEntity<EditNoticeResponse> updateNotice(@RequestAttribute Member member,
                                                           @PathVariable Integer noticeId,
                                                           @RequestBody EditNoticeRequest request) {
        Notice response = noticeService.editNotice(noticeId, member, request);
        return ResponseEntity
                .status(HttpStatus.OK) //201
                .body(EditNoticeResponse.fromEntity(response));
    }

    @Operation(summary = "공지사항 삭제", description = "기존 공지사항을 삭제합니다.")
    @DeleteMapping("/{noticeId}")
    public ResponseEntity<DeleteNoticeResponse> deleteNotice(@RequestAttribute Member member,
                                                             @PathVariable Integer noticeId) {
        noticeService.deleteNotice(member, noticeId);

        return ResponseEntity
                .status(HttpStatus.OK) //201
                .body(DeleteNoticeResponse.fromEntity());
    }

    @Operation(summary = "공지사항 전체 목록 조회", description = "모든 공지사항을 조회합니다.")
    @GetMapping
    public ResponseEntity<SearchAllNoticeResponse> getNotice(@RequestAttribute Member member,
                                                             @RequestParam(required = false) String keyword) {
        SearchAllNoticeResponse response = noticeService.searchAllNotice(member, keyword);

        return ResponseEntity
                .status(HttpStatus.OK) //200
                .body(response);
    }

    @Operation(summary = "공지사항 상세 조회", description = "공지사항을 ID로 조회합니다.")
    @GetMapping("/{noticeId}")
    public ResponseEntity<SearchNoticeByIdResponse> searchNoticeById(@PathVariable Integer noticeId,
                                                                     @RequestAttribute Member member) {

        SearchNoticeByIdResponse response = noticeService.searchNoticeById(member, noticeId);

        return ResponseEntity
                .status(HttpStatus.OK) //200
                .body(response);
    }

    @Operation(summary = "공지사항 고정/고정해제", description = "공지사항을 고정하거나 고정해제합니다.")
    @PatchMapping("/{noticeId}/pin")
    public ResponseEntity<PinNoticeResponse> pinNotice(@RequestAttribute Member member,
                                                       @PathVariable Integer noticeId) {
        PinNoticeResponse response = noticeService.pinNotice(noticeId, member);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @Operation(summary = "공지사항 파일 업로드", description = "공지사항에 파일을 업로드합니다.")
    @PostMapping("/{noticeId}/attachment")
    public ResponseEntity<List<NoticeAttachment>> uploadPostFiles(@RequestAttribute Member member,
                                                                  @PathVariable Integer noticeId,
                                                                  @RequestParam("files") List<MultipartFile> files) {

        List<NoticeAttachment> response = noticeService.uploadFiles(member, noticeId, files);

        return ResponseEntity
                .status(HttpStatus.OK) //200
                .body(response);
    }

    @Operation(summary = "공지사항 첨부파일 다운로드", description = "공지사항 첨부파일을 다운로드합니다.")
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

    @Operation(summary = "공지 알림 조회", description = "멤버의 공지 알림 목록을 조회합니다.")
    @GetMapping("/notification")
    public ResponseEntity<List<NoticeNotificationResponse>> getNoticeNotifications(@RequestAttribute Member member) {
        List<NoticeNotificationResponse> response = noticeService.getNoticeNotifications(member);
        return ResponseEntity
                .status(HttpStatus.OK) //200
                .body(response);
    }
}
