package com.example.cellphoneback.repository.member;

import com.example.cellphoneback.entity.member.Member;
import com.example.cellphoneback.entity.member.Role;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

}
