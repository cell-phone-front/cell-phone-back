package com.example.cellphoneback.repository.notice;

import com.example.cellphoneback.entity.notice.NoticeAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeAttachmentRepository extends JpaRepository<NoticeAttachment, String> {
}
