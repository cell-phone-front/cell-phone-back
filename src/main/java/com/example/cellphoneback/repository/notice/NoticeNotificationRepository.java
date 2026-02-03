package com.example.cellphoneback.repository.notice;

import com.example.cellphoneback.entity.notice.NoticeNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeNotificationRepository extends JpaRepository<NoticeNotification, Integer> {

}
