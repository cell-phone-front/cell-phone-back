
package com.example.cellphoneback.dto.response.member;

import com.example.cellphoneback.entity.member.Member;
import com.example.cellphoneback.entity.member.Role;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class CreateMemberResponse {
    Member member;
    String message;

}
