package com.example.cellphoneback.controller.member;

import com.example.cellphoneback.dto.request.member.CreateMemberRequest;
import com.example.cellphoneback.dto.request.member.MemberLoginRequest;
import com.example.cellphoneback.dto.response.member.CreateMemberResponse;
import com.example.cellphoneback.dto.response.member.MemberLoginResponse;
import com.example.cellphoneback.entity.member.Member;
import com.example.cellphoneback.service.member.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@SecurityRequirement(name = "bearerAuth")
//@Tag(name = "Comment", description = "댓글 관련 API")
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {
    final MemberService memberService;

    // POST /api/member/login	로그인 all
    @PostMapping("/login")
    public ResponseEntity<MemberLoginResponse> memberLogin(@RequestBody MemberLoginRequest request){
        MemberLoginResponse response = memberService.memberLoginService(request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // POST	/api/member/create	계정 생성 admin
    @PostMapping("/create")
    public ResponseEntity<?> memberCreate(@RequestBody CreateMemberRequest request,
                                          @RequestAttribute Member member){

        CreateMemberResponse response = memberService.createMemberService(member, request);

        return ResponseEntity.status(HttpStatus.OK).body(response);

    }


    // POST	/api/member/parse/xls	멤버 엑셀 파싱 admin


    // GET /api/member	전체 멤버 조회	admin


    // PUT /api/member	멤버 정보 수정	admin


    // DELETE /api/member	멤버 정보 삭제	admin


    // GET /api/member/{memberId}	계정 조회	본인


}
