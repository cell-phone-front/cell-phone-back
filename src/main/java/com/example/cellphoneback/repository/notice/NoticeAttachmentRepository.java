package com.example.cellphoneback.repository.notice;

import com.example.cellphoneback.entity.notice.NoticeAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeAttachmentRepository extends JpaRepository<NoticeAttachment, Long> {
    List<NoticeAttachment> findByNoticeId(Integer noticeId);

    List<NoticeAttachment> findByNoticeIdAndId(Integer noticeId, String noticeAttachmentId);
}