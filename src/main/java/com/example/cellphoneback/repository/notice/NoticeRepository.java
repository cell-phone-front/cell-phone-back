package com.example.cellphoneback.repository.notice;

import com.example.cellphoneback.entity.notice.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Integer> {
    long countByPinnedTrue();

    @Modifying
    @Query("update  Notice n set n.viewCount = n.viewCount +1 where n.id=:id")
    void increaseViewCount(@Param("id") Integer id);

}
