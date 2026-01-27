package com.example.cellphoneback.dto.request.member;


import com.example.cellphoneback.entity.member.Member;
import com.example.cellphoneback.entity.member.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class MemberBulkUpsertRequest {

    List<Item> memberList;

    @Getter
    @Setter
    static public class Item {
        private String id;
        private String name;
        private String email;
        private String phoneNumber;
        private String dept;
        private String workTeam;
        private String role;
        private String hireDate;


        public Member toEntity() {
            return Member.builder()
                    .id(this.id)
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



}
