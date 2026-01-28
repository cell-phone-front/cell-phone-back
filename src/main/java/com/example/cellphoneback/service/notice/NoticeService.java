
package com.example.cellphoneback.service.notice;

import com.example.cellphoneback.dto.request.notice.CreateNoticeRequest;
import com.example.cellphoneback.dto.request.notice.EditNoticeRequest;
import com.example.cellphoneback.entity.member.Member;
import com.example.cellphoneback.entity.member.Role;
import com.example.cellphoneback.entity.notice.Notice;
import com.example.cellphoneback.repository.notice.NoticeRepository;
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
        notice.setDescription(request.getDescription());

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
    public List<Notice> searchAllNotice(Member member, String keyword) {

        List<Notice> notices = noticeRepository.findAll();

        if (notices.isEmpty()) {
            throw new IllegalArgumentException("존재하는 공지사항이 없습니다.");
        }


        // 정렬 - 최신순, 검색
        List<Notice> noticeList = notices.stream()
                .sorted(Comparator.comparing(Notice::getCreatedAt).reversed())
                .filter(c -> {
                    if (keyword == null || keyword.isBlank())
                        return true;

                    String kw = keyword.trim();
                    return (c.getTitle() != null && c.getTitle().contains(kw)) ||
                            (c.getDescription() != null && c.getDescription().contains(kw));
                }).toList();


        return noticeList;
    }

    // community	GET	/api/notice/{noticeId}	해당 공지사항 조회	all
    public Notice searchNoticeById(Integer communityId) {

        Notice notice = noticeRepository.findById(communityId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공지사항입니다."));


        return notice;
    }
}
