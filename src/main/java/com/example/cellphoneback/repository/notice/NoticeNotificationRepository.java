package com.example.cellphoneback.repository.notice;

import com.example.cellphoneback.dto.response.notice.NoticeNotificationResponse;
import com.example.cellphoneback.entity.notice.NoticeNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeNotificationRepository extends JpaRepository<NoticeNotification, Integer> {

    List<NoticeNotificationResponse> findByMemberId(String id);
}
