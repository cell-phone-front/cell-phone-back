package com.example.cellphoneback.entity.community;

import com.example.cellphoneback.entity.member.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Community {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private String title;
    private String description;
    private LocalDateTime createdAt;
    private int commentCount;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }

}
