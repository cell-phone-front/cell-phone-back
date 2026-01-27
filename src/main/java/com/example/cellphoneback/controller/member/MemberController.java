package com.example.cellphoneback.controller.member;

import com.example.cellphoneback.dto.request.member.MemberBulkUpsertRequest;
import com.example.cellphoneback.dto.request.member.MemberLoginRequest;
import com.example.cellphoneback.dto.response.member.*;
import com.example.cellphoneback.entity.member.Member;
import com.example.cellphoneback.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    // POST	/api/member/parse/xls	멤버 엑셀 파싱 admin
    @PostMapping("/parse/xls")
    public ResponseEntity<MemberParseResponse> memberParse(@RequestBody MultipartFile file,
                                                           @RequestAttribute Member member){
        MemberParseResponse response = memberService.memberParseService(member, file);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // POST	/api/member/upsert	계정 생성, 수정, 삭제 admin
    @PostMapping("/upsert")
    public ResponseEntity<MemberBulkUpsertResponse> memberCreate(@RequestBody MemberBulkUpsertRequest request,
                                                                 @RequestAttribute Member member){

        MemberBulkUpsertResponse response = memberService.memberBulkUpsertService(member, request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    // GET /api/member	전체 멤버 조회	admin
    @GetMapping
    public ResponseEntity<MemberListResponse> MemberList(@RequestAttribute Member member){

        MemberListResponse response = memberService.memberListService(member);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }




    // GET /api/member/{memberId}	계정 조회	본인
    @GetMapping("/{memberId}")
    public ResponseEntity<MemberSearchByIdResponse> MemberByIdSearch(@RequestAttribute Member member){

        MemberSearchByIdResponse response = memberService.memberSearchByIdService(member);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}
