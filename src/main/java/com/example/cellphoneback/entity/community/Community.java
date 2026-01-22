package com.example.cellphoneback.entity.community;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Community {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String memberId;
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private int commentCount;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }

}
