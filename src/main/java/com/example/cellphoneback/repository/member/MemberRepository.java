package com.example.cellphoneback.repository.member;

import com.example.cellphoneback.entity.member.Member;

import com.example.cellphoneback.entity.member.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

    List<Member> findByRole(Role role);

}
