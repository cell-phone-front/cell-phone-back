package com.example.cellphoneback.dto.request.member;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberLoginRequest {
    private String name;
    private String id;
}
