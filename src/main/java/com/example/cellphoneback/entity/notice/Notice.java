package com.example.cellphoneback.entity.notice;

import com.example.cellphoneback.entity.member.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private String title;
    private String content;
    private LocalDateTime createdAt;
    private Boolean pinned;
    private int viewCount;

    // 첨부파일과의 관계라면 mappedBy 만 하는 것이 아니라 cascade, orphanRemoval 같이 작성
    // cascade = CascadeType.ALL -> 공지 저장/삭제 시 첨부파일도 같이 처리
    // orphanRemoval = true -> 리스트에서 제거하면 DB에서도 삭제
    @OneToMany(mappedBy = "notice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NoticeAttachment> noticeAttachmentList;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }


}
