
package com.example.cellphoneback.service.notice;

import com.example.cellphoneback.dto.request.notice.CreateNoticeRequest;
import com.example.cellphoneback.dto.request.notice.EditNoticeRequest;
import com.example.cellphoneback.dto.response.notice.NoticeNotificationResponse;
import com.example.cellphoneback.dto.response.notice.PinNoticeResponse;
import com.example.cellphoneback.dto.response.notice.SearchAllNoticeResponse;
import com.example.cellphoneback.dto.response.notice.SearchNoticeByIdResponse;
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
import java.util.*;

@RequiredArgsConstructor
@Service
public class NoticeService {
    private final NoticeRepository noticeRepository;
    private final NoticeAttachmentRepository noticeAttachmentRepository;
    private final MemberRepository memberRepository;
    private final NoticeNotificationRepository noticeNotificationRepository;

    //   1	notice	POST	/api/notice	공지사항 작성	admin, planner
    public Notice createNotice(Member member, CreateNoticeRequest request) {

        if (!member.getRole().equals(Role.ADMIN) && !member.getRole().equals(Role.PLANNER)) {
            throw new SecurityException("공지사항 작성 권한이 없습니다.");
        }

        Notice notice = request.toEntity();
        notice.setMember(member);
        notice.setPinned(request.getPinned());
        Notice savedNotice = noticeRepository.save(notice);

        List<Member> members = memberRepository.findAll();
        List<NoticeNotification> notifications = new ArrayList<>();
        for (Member m : members) {

            // ✅ 작성자 제외
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

    //2	notice	PUT	/api/notice	공지사항 수정	admin, planner
    public Notice editNotice(Integer noticeId, Member member, EditNoticeRequest request) {

        if (!member.getRole().equals(Role.ADMIN) && !member.getRole().equals(Role.PLANNER)) {
            throw new SecurityException("공지사항 작성 권한이 없습니다.");
        }

        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공지사항입니다."));


        notice.setTitle(request.getTitle());
        notice.setContent(request.getContent());

        return noticeRepository.save(notice);
    }

    //3	notice	DELETE	/api/notice	공지사항 삭제	admin, planner
    public void deleteNotice(Member member, Integer noticeId) {

        if (!member.getRole().equals(Role.ADMIN) && !member.getRole().equals(Role.PLANNER)) {
            throw new SecurityException("공지사항 삭제 권한이 없습니다.");
        }

        List<NoticeNotification> notification = noticeNotificationRepository.findAll().stream()
                .filter(e -> e.getNoticeId() == noticeId).toList();

        noticeNotificationRepository.deleteAll(notification);

        List<NoticeAttachment> attachments = noticeAttachmentRepository.findAll().stream()
                .filter(e -> e.getNoticeId() == noticeId).toList();

        noticeAttachmentRepository.deleteAll(attachments);

        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공지사항입니다."));


        noticeRepository.delete(notice);
    }

    //4	notice	GET	/api/notice	전체 공지사항 조회	all
    public SearchAllNoticeResponse searchAllNotice(Member member, String keyword) {

        long totalNoticeCount = noticeRepository.count();

        List<Notice> notices = noticeRepository.findAll();

        if (notices.isEmpty()) {
            throw new IllegalArgumentException("존재하는 공지사항이 없습니다.");
        }


        // 정렬 - 최신순, 검색
        List<SearchNoticeByIdResponse> noticeList = notices.stream()
                .sorted(Comparator.comparing((Notice n) -> n.getPinned() != null && n.getPinned())
                        .thenComparing(Notice::getCreatedAt).reversed())
                .filter(c -> {
                    if (keyword == null || keyword.isBlank())
                        return true;

                    String kw = keyword.trim();
                    return (c.getTitle() != null && c.getTitle().contains(kw)) ||
                            (c.getContent() != null && c.getContent().contains(kw));
                })
                .map(n->SearchNoticeByIdResponse.fromEntity(n, null))
                .toList();


        return SearchAllNoticeResponse.builder()
                .totalNoticeCount(totalNoticeCount)
                .noticeList(noticeList)
                .build();
    }

    // GET	/api/notice/{noticeId}	해당 공지사항 조회	all
    @Transactional
    public SearchNoticeByIdResponse searchNoticeById(Member member, Integer noticeId) {

        noticeRepository.increaseViewCount(noticeId);

        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공지사항입니다."));


        noticeNotificationRepository.markAsRead(member.getId(), noticeId);

        List<NoticeAttachment> attachments = noticeAttachmentRepository.findByNoticeId(noticeId);

        return SearchNoticeByIdResponse.fromEntity(notice, attachments);
    }

    // PATCH	/api/notice/{noticeId}/pin	공지사항 핀 고정	admin, planner
    public PinNoticeResponse pinNotice(Integer noticeId, Member member) {

        if (!member.getRole().equals(Role.ADMIN) && !member.getRole().equals(Role.PLANNER)) {
            throw new IllegalArgumentException("공지사항 핀 고정 권한이 없습니다.");
        }
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공지사항입니다."));


        boolean currentPin = notice.getPinned(); // 현재 핀 상태
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
            throw new IllegalArgumentException("공지사항 파일 첨부 권한이 없습니다.");
        }

        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new NoSuchElementException("공지사항 없음"));

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

        for (MultipartFile file : files) {
            if (file.isEmpty()) continue;

            String savedFileName = UUID.randomUUID().toString()
                    .replace("-", "") + "_" + file.getOriginalFilename();

            try {
                file.transferTo(uploadPath.resolve(savedFileName));
            } catch (IOException e) {
                throw new RuntimeException("파일 저장 실패", e); //500
            }

            NoticeAttachment attachment = NoticeAttachment.builder()
                    .noticeId(noticeId)
                    .fileSize(file.getSize())
                    .fileType(file.getContentType())
                    .fileUrl(savedFileName)
                    .build();

            attachments.add(attachment);
        }

        noticeAttachmentRepository.saveAll(attachments);

        return attachments;
    }

    // notice	GET	/api/notice/{noticeId}/attachment/{noticeAttachmentId}	공지사항 파일 다운로드 admin, planner
    public Path getNoticeAttachmentPath(Member member, Integer noticeId, String noticeAttachmentId) {

        noticeRepository.findById(noticeId)
                .orElseThrow(() -> new NoSuchElementException("공지사항 없음"));

        NoticeAttachment attachment = noticeAttachmentRepository.findByNoticeIdAndId(noticeId, noticeAttachmentId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("첨부파일 없음"));


        Path uploadPath = Path.of(System.getProperty("user.home"), "cellphone", "notice", String.valueOf(noticeId));
        Path filePath = uploadPath.resolve(attachment.getFileUrl());

        if (!Files.exists(filePath)) {
            throw new NoSuchElementException("파일이 서버에 존재하지 않습니다.");
        }

        return filePath;
    }

    // notice	GET	/api/notice/notification	공지사항 알림 조회	all	pathvariable = memberId
    public List<NoticeNotificationResponse> getNoticeNotifications(Member member) {

        return noticeNotificationRepository.findByMemberId(member.getId());

    }

}
