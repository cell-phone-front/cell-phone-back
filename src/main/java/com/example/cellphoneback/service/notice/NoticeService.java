
package com.example.cellphoneback.service.notice;

import com.example.cellphoneback.dto.request.notice.CreateNoticeRequest;
import com.example.cellphoneback.dto.request.notice.EditNoticeRequest;
import com.example.cellphoneback.dto.response.notice.PinNoticeResponse;
import com.example.cellphoneback.dto.response.notice.SearchAllNoticeResponse;
import com.example.cellphoneback.dto.response.notice.SearchNoticeByIdResponse;
import com.example.cellphoneback.entity.member.Member;
import com.example.cellphoneback.entity.member.Role;
import com.example.cellphoneback.entity.notice.Notice;
import com.example.cellphoneback.repository.notice.NoticeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class NoticeService {
    private final NoticeRepository noticeRepository;

    //   1	notice	POST	/api/notice	공지사항 작성	admin, planner
    public Notice createNotice(Member member, CreateNoticeRequest request) {

        if (!member.getRole().equals(Role.ADMIN) && !member.getRole().equals(Role.PLANNER)) {
            throw new SecurityException("공지사항 작성 권한이 없습니다.");
        }

        Notice notice = request.toEntity();
        notice.setMember(member);

        return noticeRepository.save(notice);
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

        if (!member.getRole().equals(Role.PLANNER) && !member.getRole().equals(Role.WORKER)) {
            throw new SecurityException("공지사항 삭제 권한이 없습니다.");
        }

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
                .sorted(Comparator.comparing(Notice::getCreatedAt).reversed())
                .filter(c -> {
                    if (keyword == null || keyword.isBlank())
                        return true;

                    String kw = keyword.trim();
                    return (c.getTitle() != null && c.getTitle().contains(kw)) ||
                            (c.getContent() != null && c.getContent().contains(kw));
                })
                .map(SearchNoticeByIdResponse::fromEntity)
                .toList();


        return SearchAllNoticeResponse.builder()
                .totalNoticeCount(totalNoticeCount)
                .noticeList(noticeList)
                .build();
    }

    // GET	/api/notice/{noticeId}	해당 공지사항 조회	all
    @Transactional
    public Notice searchNoticeById(Integer communityId) {

        noticeRepository.increaseViewCount(communityId);

        Notice notice = noticeRepository.findById(communityId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공지사항입니다."));


        return notice;
    }

    // PATCH	/api/notice/{noticeId}/pin	공지사항 핀 고정	admin, planner
    public Notice pinNotice(Integer noticeId, Member member) {

        if (!member.getRole().equals(Role.ADMIN) && !member.getRole().equals(Role.PLANNER)) {
            throw new IllegalArgumentException("공지사항 핀 고정 권한이 없습니다.");
        }
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공지사항입니다."));


        boolean currentPin = notice.isPinned(); // 현재 핀 상태
        if(!currentPin){
            long pinnedCount = noticeRepository.countByPinnedTrue(); // 현재 핀 고정된 공지사항 수
            if(pinnedCount >= 3){
                throw new IllegalStateException("최대 3개의 공지사항만 핀 고정할 수 있습니다.");
            }
        }
        notice.setPinned(!currentPin);

        return noticeRepository.save(notice);
    }
}
