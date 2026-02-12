package com.example.cellphoneback.entity.notice;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class NoticeAttachment {

    @Id
    private String id;

    @ManyToOne
    @JsonIgnore
    private Notice notice;

    private long fileSize;
    private String fileUrl;
    private String fileType;

    @PrePersist
    public void prePersist() {
        this.id = UUID.randomUUID().toString().substring(0, 6);
    }


}
