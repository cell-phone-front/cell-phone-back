package com.example.cellphoneback.entity.notice;

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

    private String memberId;
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private boolean pinned;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }
}
