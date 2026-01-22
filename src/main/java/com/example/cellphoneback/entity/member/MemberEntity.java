package com.example.cellphoneback.entity.member;

import jakarta.persistence.PrePersist;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberEntity {

    private String id;
    private String name;
    private String email;
    private int phoneNumber;
    private String dept;
    private String workTeam;
    private Role role;
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }
}
