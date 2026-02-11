package com.example.cellphoneback.repository.notice;

import com.example.cellphoneback.dto.response.notice.NoticeNotificationResponse;
import com.example.cellphoneback.entity.notice.NoticeNotification;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeNotificationRepository extends JpaRepository<NoticeNotification, Integer> {

    List<NoticeNotificationResponse> findByMemberId(String id);

    // 읽음 처리를 하기 위한 로직
    @Modifying
    @Query("""
    update NoticeNotification n 
    set n.isRead = true
    where n.memberId = :memberId
    and n.noticeId = :noticeId
    and n.isRead = false 
""")
    int markAsRead(@Param("memberId") String memberId, @Param("noticeId") int noticeId);
}
