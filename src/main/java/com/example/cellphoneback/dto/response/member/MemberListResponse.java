package com.example.cellphoneback.dto.response.member;

import com.example.cellphoneback.entity.member.Member;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MemberListResponse {
    List<Member> memberList;
}
