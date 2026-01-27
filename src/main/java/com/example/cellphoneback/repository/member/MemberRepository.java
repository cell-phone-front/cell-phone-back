package com.example.cellphoneback.repository.member;

import com.example.cellphoneback.entity.member.Member;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

}
