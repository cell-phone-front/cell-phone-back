package com.example.cellphoneback.controller.member;

import com.example.cellphoneback.dto.request.member.MemberBulkUpsertRequest;
import com.example.cellphoneback.dto.request.member.MemberLoginRequest;
import com.example.cellphoneback.dto.response.member.*;
import com.example.cellphoneback.entity.member.Member;
import com.example.cellphoneback.service.member.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Member", description = "멤버 관련 API")
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {
    final MemberService memberService;

    @Operation(summary = "멤버 로그인", description = "멤버가 시스템에 로그인합니다. 모든 사용자가 접근할 수 있습니다.")
    @PostMapping("/login")
    public ResponseEntity<MemberLoginResponse> memberLogin(@RequestBody MemberLoginRequest request){
        MemberLoginResponse response = memberService.memberLoginService(request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "멤버 엑셀 파싱", description = "엑셀 파일을 파싱하여 멤버 정보를 추출합니다. 관리자(ADMIN) 권한 필요.")
    @PostMapping("/parse/xls")
    public ResponseEntity<MemberParseResponse> memberParse(@RequestBody MultipartFile file,
                                                           @RequestAttribute Member member){
        MemberParseResponse response = memberService.memberParseService(member, file);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "멤버 일괄 업서트", description = "멤버 정보를 일괄적으로 추가 또는 업데이트합니다. 관리자(ADMIN) 권한 필요.")
    @PostMapping("/upsert")
    public ResponseEntity<MemberBulkUpsertResponse> memberUpsert(@RequestBody MemberBulkUpsertRequest request,
                                                                 @RequestAttribute Member member){

        MemberBulkUpsertResponse response = memberService.memberBulkUpsertService(member, request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "멤버 목록 조회", description = "멤버 목록을 조회합니다. 키워드로 필터링할 수 있습니다. 관리자(ADMIN) 권한 필요.")
    @GetMapping
    public ResponseEntity<MemberListResponse> MemberList(@RequestAttribute Member member,
                                                         @RequestParam(required = false) String keyword){

        MemberListResponse response = memberService.memberListService(member, keyword);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "멤버 상세 조회", description = "멤버 ID로 멤버의 상세 정보를 조회합니다. 관리자(ADMIN) 권한 필요.")
    @GetMapping("/{memberId}")
    public ResponseEntity<MemberSearchByIdResponse> MemberByIdSearch(@RequestAttribute Member member,
                                                                     @PathVariable String memberId){

        MemberSearchByIdResponse response = memberService.memberSearchByIdService(member,  memberId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}
