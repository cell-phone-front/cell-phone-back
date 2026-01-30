package com.example.cellphoneback.repository.notice;

import com.example.cellphoneback.entity.notice.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Integer> {
    long countByPinnedTrue();
}
