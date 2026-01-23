
package com.example.cellphoneback.service.member;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.cellphoneback.dto.request.member.CreateMemberRequest;
import com.example.cellphoneback.dto.request.member.MemberLoginRequest;
import com.example.cellphoneback.dto.response.member.CreateMemberResponse;
import com.example.cellphoneback.dto.response.member.MemberLoginResponse;
import com.example.cellphoneback.entity.member.Member;
import com.example.cellphoneback.entity.member.Role;
import com.example.cellphoneback.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;


@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;

    // POST	/api/member/login	로그인	all
    public MemberLoginResponse memberLoginService(MemberLoginRequest request){
        Member member = memberRepository.findById(request.getId())
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 멤버입니다."));

        if(!member.getName().equals(request.getName())){
            throw new IllegalStateException("정보가 일치하지 않습니다.");
        }

        String token = JWT.create()
                .withSubject(member.getId())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .withIssuer("cellPhone")
                .sign(Algorithm.HMAC256("phoneKey"));

        return MemberLoginResponse.builder().member(member).token(token).build();

    }

    // POST	/api/member/create	계정 생성	admin
    public CreateMemberResponse createMemberService(Member member, CreateMemberRequest request) {

        if (!member.getRole().equals(Role.ADMIN)) {
            throw new SecurityException("생성 권한이 없습니다.");
        }

        Member members =  request.toEntity();
        memberRepository.save(members);
        return CreateMemberResponse.builder().member(members).message("성공적 등록 했습니다.").build();
    }

    // POST	/api/member/parse/xls	멤버 엑셀 파싱	admin


    // GET	/api/member	전체 멤버 조회	admin


    // PUT	/api/member	멤버 정보 수정	admin


    // DELETE	/api/member	멤버 정보 삭제	admin


    // GET	/api/member/{memberId}	계정 조회	본인


}
