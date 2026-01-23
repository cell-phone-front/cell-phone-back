package com.example.cellphoneback.entity.member;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

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
    @Email
    private String email;
    private String phoneNumber;
    private String dept;
    private String workTeam;
    @Enumerated(EnumType.STRING)
    private Role role;
    private String hireDate;

    @PrePersist
    public void prePersist() {
        this.id = UUID.randomUUID().toString().substring(0, 6);
    }

}
