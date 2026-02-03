package com.example.cellphoneback.entity.notice;

import com.example.cellphoneback.entity.member.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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
    private boolean pinned;
    private int viewCount;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }


}
