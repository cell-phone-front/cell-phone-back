package com.example.cellphoneback.entity.member;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Member {

    @Id
    private String id;

    private String name;
    private String email;
    private int phoneNumber;
    private String dept;
    private String workTeam;
    private Role role;
    private LocalDateTime createdAt;

}
