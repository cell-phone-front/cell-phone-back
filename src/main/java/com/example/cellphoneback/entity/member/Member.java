package com.example.cellphoneback.entity.member;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

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
    private Boolean isActive;

    @PrePersist
    public void prePersist() {
        if(this.id == null || this.id.isBlank()) {
            this.id = UUID.randomUUID().toString().substring(0, 6);
        }
        if (this.isActive == null) {
            this.isActive = true;
        }
    }

}
