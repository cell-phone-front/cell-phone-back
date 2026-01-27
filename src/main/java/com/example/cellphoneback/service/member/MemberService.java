
package com.example.cellphoneback.service.member;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.cellphoneback.dto.request.member.MemberBulkUpsertRequest;
import com.example.cellphoneback.dto.request.member.MemberLoginRequest;
import com.example.cellphoneback.dto.response.member.*;
import com.example.cellphoneback.entity.member.Member;
import com.example.cellphoneback.entity.member.Role;
import com.example.cellphoneback.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;


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

    // POST	/api/member/parse/xls	멤버 엑셀 파싱	admin
    public MemberParseResponse memberParseService(Member member, MultipartFile memberFile){
        if (!member.getRole().equals(Role.ADMIN)) {
            throw new SecurityException("ADMIN 권한이 없습니다.");
        }

        if (memberFile.isEmpty()) {
            throw new NoSuchElementException("파일 내용이 존재하지 않습니다.");
        }

        try {
            // 파일을 Apache POI가 이해할 수 있는 WorkBook으로 변환
            Workbook workbook = WorkbookFactory.create(memberFile.getInputStream());
            // 첫번째 sheet 선택
            Sheet sheet = workbook.getSheetAt(0);
            // 시트에 존재하는 모든 행을 위에서부터 하나씩 읽기 위한 반복자
            Iterator<Row> iterator = sheet.iterator();
            // id,email 등 컬럼이 첫행이라고 보고 분리
            Row header = iterator.next();

            DataFormatter formatter = new DataFormatter();
            List<MemberParseResponse.xls> memberXls = new ArrayList<>();
            while (iterator.hasNext()) {
                Row row = iterator.next();

                MemberParseResponse.xls one =
                        MemberParseResponse.xls.builder()
                                .id(UUID.randomUUID().toString().substring(0,6))
                                .name(formatter.formatCellValue(row.getCell(1)))
                                .email(formatter.formatCellValue(row.getCell(2)))
                                .phoneNumber(formatter.formatCellValue(row.getCell(3)))
                                .dept(formatter.formatCellValue(row.getCell(4)))
                                .workTeam(formatter.formatCellValue(row.getCell(5)))
                                .role(formatter.formatCellValue(row.getCell(6)))
                                .hireDate(formatter.formatCellValue(row.getCell(7)))
                                .build();
                memberXls.add(one);
            }
            return MemberParseResponse.builder().memberList(memberXls).build();

        } catch (IOException e) {
            throw new RuntimeException("파일 처리 중 오류가 발생했습니다.");
        }
    }

    // POST	/api/member/upsert	계정 생성, 수정, 삭제	admin
    public MemberBulkUpsertResponse memberBulkUpsertService(Member member, MemberBulkUpsertRequest request) {
        if (!member.getRole().equals(Role.ADMIN)) {
            throw new SecurityException("ADMIN 권한이 없습니다.");
        }
        List<MemberBulkUpsertRequest.Item> items = request.getMemberList();
        List<String> itemIds = items.stream().map(e -> e.toEntity().getId()).toList();

        List<Member> saveMember = memberRepository.findAll();
        List<Member> notContainsMember =
                saveMember.stream()
                        .filter(e -> !itemIds.contains(e.getId())).toList();
        memberRepository.deleteAll(notContainsMember);

        List<Member> upsertMembers = items.stream().map(e -> Member.builder()
                .id(e.toEntity().getId())
                .name(e.toEntity().getName())
                .email(e.toEntity().getEmail())
                .phoneNumber(e.toEntity().getPhoneNumber())
                .dept(e.toEntity().getDept())
                .workTeam(e.toEntity().getWorkTeam())
                .role(e.toEntity().getRole())
                .hireDate(e.toEntity().getHireDate()).build()).toList();
        memberRepository.saveAll(upsertMembers);

        int deleted = notContainsMember.size();
        int updated = saveMember.size() - deleted;
        int created = upsertMembers.size() - updated;

        return MemberBulkUpsertResponse.builder()
                .createMember(created)
                .deleteMember(deleted)
                .updateMember(updated).build();
    }


    // GET	/api/member	전체 멤버 조회	admin
    public MemberListResponse memberListService(Member member) {
        if (!member.getRole().equals(Role.ADMIN)) {
            throw new SecurityException("ADMIN 권한이 없습니다.");
        }

        List<Member> memberList = memberRepository.findAll();
        return MemberListResponse.builder().memberList(memberList).build();
    }

    // GET	/api/member/{memberId}	계정 조회	본인
    public MemberSearchByIdResponse memberSearchByIdService(Member member){
        Member self = memberRepository.findById(member.getId())
                .orElseThrow(() -> new NoSuchElementException("찾을 수 없습니다."));

        return MemberSearchByIdResponse.builder().member(self).build();
    }


}
