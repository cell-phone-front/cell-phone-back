package com.example.cellphoneback.dto.response.member;

import com.example.cellphoneback.entity.member.Role;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MemberParseResponse {
    List<xls> memberList;

    @Getter
    @Builder
    @JsonPropertyOrder({"id", "name", "email", "phoneNumber", "dept", "workTeam", "role", "hireDate"})
    public static class xls{
        private String id;
        private String name;
        private String email;
        private String phoneNumber;
        private String dept;
        private String workTeam;
        private String role;
        private String hireDate;
    }
}
