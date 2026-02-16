
package com.example.cellphoneback.service.notice;

import com.example.cellphoneback.dto.request.notice.CreateNoticeRequest;
import com.example.cellphoneback.dto.request.notice.EditNoticeRequest;
import com.example.cellphoneback.dto.response.notice.*;
import com.example.cellphoneback.entity.member.Member;
import com.example.cellphoneback.entity.member.Role;
import com.example.cellphoneback.entity.notice.Notice;
import com.example.cellphoneback.entity.notice.NoticeAttachment;
import com.example.cellphoneback.entity.notice.NoticeNotification;
import com.example.cellphoneback.repository.member.MemberRepository;
import com.example.cellphoneback.repository.notice.NoticeAttachmentRepository;
import com.example.cellphoneback.repository.notice.NoticeNotificationRepository;
import com.example.cellphoneback.repository.notice.NoticeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@Service
public class NoticeService {
    private final NoticeRepository noticeRepository;
    private final NoticeAttachmentRepository noticeAttachmentRepository;
    private final MemberRepository memberRepository;
    private final NoticeNotificationRepository noticeNotificationRepository;

    //   1	notice	POST	/api/notice	공지사항 작성	admin, planner
    @Transactional
    public Notice createNotice(Member member, CreateNoticeRequest request, List<MultipartFile> files) {

        if (!member.getRole().equals(Role.ADMIN) && !member.getRole().equals(Role.PLANNER)) {
            throw new SecurityException("ADMIN, PLANNER 권한이 없습니다.");
        }

        Notice notice = request.toEntity();
        notice.setMember(member);

        notice.setPinned(Boolean.TRUE.equals(request.getPinned()));

        if(notice.getCreatedAt() == null) {
            notice.setCreatedAt(LocalDateTime.now());
        }

        Notice savedNotice = noticeRepository.save(notice);

        if(files != null &&  !files.isEmpty()) {
            uploadFiles(member, savedNotice.getId(), files);
        }

        // 작성자 제외
        List<Member> members = memberRepository.findAll();
        List<NoticeNotification> notifications = new ArrayList<>();
        for (Member m : members) {

            if (m.getId().equals(member.getId())) continue;

            NoticeNotification noticeNotification = NoticeNotification.builder()
                    .memberId(m.getId())
                    .noticeId(savedNotice.getId())
                    .message("새로운 공지사항이 등록되었습니다.")
                    .link("/notices/" + savedNotice.getId())
                    .isRead(false)
                    .build();

            notifications.add(noticeNotification);
        }

        noticeNotificationRepository.saveAll(notifications);

        return savedNotice;
    }

    //2	notice	PUT	/api/notice/{noticeId}	공지사항 수정	admin, planner
    @Transactional
    public EditNoticeResponse editNotice(Integer noticeId, Member member, EditNoticeRequest request, List<MultipartFile> files) {

        System.out.println(request.getDeleteAttachmentIds());
        if (!member.getRole().equals(Role.ADMIN) && !member.getRole().equals(Role.PLANNER)) {
            throw new SecurityException("ADMIN, PLANNER 권한이 없습니다.");
        }

        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공지사항입니다."));

        notice.setTitle(request.getTitle());
        notice.setContent(request.getContent());

        if (request.getPinned() != null) {
            notice.setPinned(request.getPinned());
        }

        // 삭제할 ID 리스트가 null 아니거나 비어있지 않다면 해당 noticeId로 조회하고 request로 받은 ID에 포함되어 있다면 삭제
        if (request.getDeleteAttachmentIds() != null && !request.getDeleteAttachmentIds().isEmpty()) {

            List<NoticeAttachment> attachments =
                    noticeAttachmentRepository.findByNoticeId(noticeId);

            for (NoticeAttachment attachment : attachments) {
                if (request.getDeleteAttachmentIds().contains(attachment.getId())) {
                    noticeAttachmentRepository.delete(attachment);
                }
            }
        }

        // 파일이 null이 아니고 비어있지 않다면 업로드
        if(files != null && !files.isEmpty()) {
            uploadFiles(member, noticeId, files);
        }

        List<NoticeAttachment> attachments = noticeAttachmentRepository.findByNoticeId(noticeId);

        return EditNoticeResponse.fromEntity(notice,attachments );
    }


    //3	notice	DELETE	/api/notice/{noticeId}	공지사항 삭제	admin, planner
    public void deleteNotice(Member member, Integer noticeId) {

        if (!member.getRole().equals(Role.ADMIN) && !member.getRole().equals(Role.PLANNER)) {
            throw new SecurityException("ADMIN, PLANNER 권한이 없습니다.");
        }

        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공지사항입니다."));

        noticeRepository.delete(notice);

        Path uploadPath = Path.of(System.getProperty("user.home"),
                "cellphone",
                "notice",
                String.valueOf(noticeId));
        try {
            if (Files.exists(uploadPath)) {
                // 폴더 안 파일 먼저 삭제 후 폴더 삭제
                Files.walk(uploadPath)
                        .sorted(Comparator.reverseOrder())
                        .forEach(p -> {
                            try {
                                Files.deleteIfExists(p);
                            } catch (IOException ex) {
                                throw new RuntimeException("파일 삭제 실패", ex);
                            }
                        });
            }
        } catch (IOException e) {
            throw new RuntimeException("업로드 폴더 삭제 실패", e);
        }
    }

    //4	notice	GET	/api/notice	전체 공지사항 조회	all
    public SearchAllNoticeResponse searchAllNotice(String keyword) {

        List<Notice> notices = noticeRepository.findAll();

        if (notices.isEmpty()) {
            return SearchAllNoticeResponse.builder()
                    .totalNoticeCount(0)
                    .noticeList(List.of()).build();
        }
        String k = (keyword == null) ? "" : keyword.trim().toLowerCase().replaceAll("\\s+", "");

        // Notice 리스트로 필터 + 정렬 (핀 우선, 최신순)
        List<NoticeListResponse> noticeList = notices.stream()
                .filter(n -> {
                    if (k.isBlank()) return true;

                    String t = (n.getTitle() == null) ? "" : n.getTitle().toLowerCase().replaceAll("\\s+", "");
                    String c = (n.getContent() == null) ? "" : n.getContent().toLowerCase().replaceAll("\\s+", "");
                    return t.contains(k) || c.contains(k);
                })
                .sorted(
                        Comparator
                                .comparing((Notice n) -> Boolean.TRUE.equals(n.getPinned()))
                                .thenComparing(Notice::getCreatedAt, Comparator.nullsLast(Comparator.naturalOrder()))
                                .reversed()
                )
                .map(n -> NoticeListResponse.fromEntity(n))
                .toList();

        // count는 검색 결과 수
        long totalNoticeCount = noticeList.size();

        return SearchAllNoticeResponse.builder()
                .totalNoticeCount(totalNoticeCount)
                .noticeList(noticeList)
                .build();
    }

    // GET	/api/notice/{noticeId}	해당 공지사항 조회	all
    @Transactional
    public SearchNoticeByIdResponse searchNoticeById(Member member, Integer noticeId) {

        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공지사항입니다."));

        noticeRepository.increaseViewCount(noticeId);
        if (member != null) {
            noticeNotificationRepository.markAsRead(member.getId(), noticeId);
        }

        List<NoticeAttachment> attachments = noticeAttachmentRepository.findByNoticeId(noticeId);

        return SearchNoticeByIdResponse.fromEntity(notice, attachments);
    }

    // PATCH	/api/notice/{noticeId}/pin	공지사항 핀 고정	admin, planner
    @Transactional
    public PinNoticeResponse pinNotice(Integer noticeId, Member member) {

        if (!member.getRole().equals(Role.ADMIN) && !member.getRole().equals(Role.PLANNER)) {
            throw new SecurityException("ADMIN, PLANNER 권한이 없습니다.");
        }
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공지사항입니다."));


        boolean currentPin = Boolean.TRUE.equals(notice.getPinned());
        if (!currentPin) {
            long pinnedCount = noticeRepository.countByPinnedTrue(); // 현재 핀 고정된 공지사항 수
            if (pinnedCount >= 3) {
                throw new IllegalStateException("최대 3개의 공지사항만 핀 고정할 수 있습니다.");
            }
        }
        notice.setPinned(!currentPin);

        noticeRepository.save(notice);

        return PinNoticeResponse.builder()
                .noticeId(notice.getId())
                .pinned(notice.getPinned())
                .build();
    }

    // notice	POST	/api/notice/{noticeId}/attachment	공지사항 파일 첨부	admin, planner	pathvariable = noticeId
    public List<NoticeAttachment> uploadFiles(Member member, Integer noticeId, List<MultipartFile> files) {

        if (!member.getRole().equals(Role.ADMIN) && !member.getRole().equals(Role.PLANNER)) {
            throw new SecurityException("ADMIN, PLANNER 권한이 없습니다.");
        }

        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new IllegalStateException("공지사항 없음"));

        if (files == null || files.isEmpty()) {
            throw new IllegalArgumentException("업로드할 파일이 없습니다."); //400
        }

        // 업로드 디렉토리 생성
        Path uploadPath = Path.of(System.getProperty("user.home"), "cellphone", "notice", String.valueOf(noticeId));

        try {
            Files.createDirectories(uploadPath);
        } catch (IOException e) {
            throw new RuntimeException("업로드 폴더 생성 실패", e); //500
        }

        // 파일 저장 및 DB 기록
        List<NoticeAttachment> attachments = new ArrayList<>();
        List<Path> savedPaths = new ArrayList<>(); // 실패 시 삭제용

        try {
            for (MultipartFile file : files) {
                if (file == null || file.isEmpty()) continue;

                String original = file.getOriginalFilename();
                String safeOriginal = (original == null ? "file" : Path.of(original).getFileName().toString());

                String savedFileName = UUID.randomUUID().toString().replace("-", "") + "_" + safeOriginal;
                Path target = uploadPath.resolve(savedFileName);

                file.transferTo(target);
                savedPaths.add(target);

                NoticeAttachment attachment = NoticeAttachment.builder()
                        .notice(notice)
                        .fileSize(file.getSize())
                        .fileType(file.getContentType())
                        .fileUrl(savedFileName)
                        .build();

                attachments.add(attachment);
            }
        } catch (IOException e) {
            // ✅ 디스크 롤백(최소)
            for (Path p : savedPaths) {
                try { Files.deleteIfExists(p); } catch (IOException ignore) {}
            }
            throw new RuntimeException("파일 저장 실패", e); // 500
        }

        if (attachments.isEmpty()) {
            throw new IllegalArgumentException("업로드할 파일이 없습니다."); // 400
        }

        noticeAttachmentRepository.saveAll(attachments);

        return attachments;
    }

    // notice	GET	/api/notice/{noticeId}/attachment/{noticeAttachmentId}	공지사항 파일 다운로드 admin, planner
    public Path getNoticeAttachmentPath(Member member, Integer noticeId, String noticeAttachmentId) {

        if (!member.getRole().equals(Role.ADMIN)
                && !member.getRole().equals(Role.PLANNER)
                && !member.getRole().equals(Role.WORKER)) {
            throw new SecurityException("다운로드 권한이 없습니다.");
        }

        noticeRepository.findById(noticeId)
                .orElseThrow(() -> new IllegalArgumentException("공지사항 없음"));

        NoticeAttachment attachment = noticeAttachmentRepository.findByNoticeIdAndId(noticeId, noticeAttachmentId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("첨부파일 없음"));


        Path uploadPath = Path.of(System.getProperty("user.home"), "cellphone", "notice", String.valueOf(noticeId));
        Path filePath = uploadPath.resolve(attachment.getFileUrl());

        if (!Files.exists(filePath)) {
            throw new IllegalArgumentException("파일이 서버에 존재하지 않습니다.");
        }

        return filePath;
    }

    // notice	GET	/api/notice/notification	공지사항 알림 조회	all	pathvariable = memberId
    public List<NoticeNotificationResponse> getNoticeNotifications(Member member) {

        return noticeNotificationRepository.findByMemberId(member.getId());

    }

}
