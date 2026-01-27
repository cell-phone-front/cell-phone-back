package com.example.cellphoneback.dto.response.member;

import com.example.cellphoneback.entity.member.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberSearchByIdResponse {
    Member member;
}
