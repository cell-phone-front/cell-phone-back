package com.example.cellphoneback.dto.request.member;


import com.example.cellphoneback.entity.member.Member;
import com.example.cellphoneback.entity.member.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
public class CreateMemberRequest {
    private String name;
    private String email;
    private String phoneNumber;
    private String dept;
    private String workTeam;
    private String role;
    private String hireDate;


    public Member toEntity() {
        return Member.builder()
                .name(this.name)
                .email(this.email)
                .phoneNumber(this.phoneNumber)
                .dept(this.dept)
                .workTeam(this.workTeam)
                .role(Role.valueOf(this.role))
                .hireDate(this.hireDate)
                .build();
    }



}
