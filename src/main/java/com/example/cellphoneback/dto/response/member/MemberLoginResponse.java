package com.example.cellphoneback.dto.response.member;


import com.example.cellphoneback.entity.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MemberLoginResponse {
    Member member;
    private String token;
}
