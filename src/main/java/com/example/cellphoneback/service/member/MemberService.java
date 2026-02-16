
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin
@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;

    // POST	/api/member/login	로그인	all
    public MemberLoginResponse memberLoginService(MemberLoginRequest request) {
        Member member = memberRepository.findById(request.getId())
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 멤버입니다."));

        if (Boolean.FALSE.equals(member.getIsActive())) {
            throw new SecurityException("비활성 계정입니다.");
        }

        if (!member.getName().equals(request.getName())) {
            throw new IllegalArgumentException("정보가 일치하지 않습니다.");
        }

        String token = JWT.create()
                .withSubject(member.getId())
                .withIssuedAt(new Date())
                //.withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .withIssuer("cellPhone")
                .sign(Algorithm.HMAC256("phoneKey"));

        return MemberLoginResponse.builder().member(member).token(token).build();

    }

    // POST	/api/member/parse/xls	멤버 엑셀 파싱	admin
    public MemberParseResponse memberParseService(Member member, MultipartFile memberFile) {
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
            iterator.next();

            DataFormatter formatter = new DataFormatter();
            List<MemberParseResponse.xls> memberXls = new ArrayList<>();
            while (iterator.hasNext()) {
                Row row = iterator.next();

                MemberParseResponse.xls one =
                        MemberParseResponse.xls.builder()
                                .id(UUID.randomUUID().toString().substring(0, 6))
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
    @Transactional
    public MemberBulkUpsertResponse memberBulkUpsertService(Member member, MemberBulkUpsertRequest request) {

        if (!member.getRole().equals(Role.ADMIN)) {
            throw new SecurityException("ADMIN 권한이 없습니다.");
        }
        List<MemberBulkUpsertRequest.Item> rawItems =
                request.getMemberList() == null ? List.of() : request.getMemberList();

        List<MemberBulkUpsertRequest.Item> items = rawItems;

        // items 를 Entity로 변환
        List<Member> reqMembers = items.stream()
                .map(e -> e.toEntity()).toList();

        List<String> itemIds = reqMembers.stream()
                .map(m -> m.getId()).toList();

        List<Member> saveMember = memberRepository.findAll();

        List<Member> notContainsMember =
                saveMember.stream()
                        .filter(e -> !itemIds.contains(e.getId())).toList();

        // 요청으로 비활성화될 수만 카운드
        int deleted = (int) notContainsMember.stream()
                .filter(m -> Boolean.TRUE.equals(m.getIsActive()))
                .count();

        // 삭제로직 지우고 비활성화로 변경하는 로직 추가
        List<Member> deactivated = notContainsMember.stream()
                .map(m -> {
                    m.setIsActive(false);
                    return m;
                }).toList();
        memberRepository.saveAll(deactivated);

        List<Member> upsertMembers = reqMembers.stream()
                .map(e -> Member.builder()
                        .id(e.getId())
                        .name(e.getName())
                        .email(e.getEmail())
                        .phoneNumber(e.getPhoneNumber())
                        .dept(e.getDept())
                        .workTeam(e.getWorkTeam())
                        .role(e.getRole())
                        .hireDate(e.getHireDate())
                        .isActive(true).build()).toList();
        memberRepository.saveAll(upsertMembers);

        // 기존 ID Set 만들기
        Set<String> existing =
                saveMember.stream()
                        .map(m -> m.getId())
                        .collect(Collectors.toSet());
        // update 계산
        int updated =
                (int) itemIds.stream()
                        .filter(id -> existing.contains(id)).count();
        // created 계산
        int created =
                (int) itemIds.stream()
                        .filter(id -> !existing.contains(id)).count();

        return MemberBulkUpsertResponse.builder()
                .createMember(created)
                .deleteMember(deleted)
                .updateMember(updated).build();
    }

    // GET	/api/member	전체 멤버 조회 및 검색 조회	admin
    public MemberListResponse memberListService(Member member, String keyword) {
        if (!member.getRole().equals(Role.ADMIN)) {
            throw new SecurityException("ADMIN 권한이 없습니다.");
        }

        List<Member> memberList = memberRepository.findAll().stream()
                .filter(m -> Boolean.TRUE.equals(m.getIsActive()))
                .filter(m -> {
                    if (keyword == null || keyword.isBlank())
                        return true;

                    String k = keyword.trim().toLowerCase().replaceAll("\\s+", "");

                    return (m.getName() != null && m.getName().toLowerCase().replaceAll("\\s+", "").contains(k))
                            || (m.getDept() != null && m.getDept().toLowerCase().replaceAll("\\s+", "").contains(k))
                            || (m.getWorkTeam() != null && m.getWorkTeam().toLowerCase().replaceAll("\\s+", "").contains(k));
                }) .sorted(
                        Comparator
                                //  Role 그룹: ADMIN -> PLANNER -> WORKER
                                .comparingInt((Member m) -> roleOrder(m.getRole()))
                                //  그룹 내 이름 정렬: 문자 먼저, 숫자는 숫자대로
                                .thenComparing((a, b) -> compareNameNatural(a.getName(), b.getName()))
                )
                .toList();
        return MemberListResponse.builder().memberList(memberList).build();


    }

    private int roleOrder(Role role) {
        if (role == Role.ADMIN) return 0;
        if (role == Role.PLANNER) return 1;
        if (role == Role.WORKER) return 2;
        return 99;
    }

    // ✅ planner1, planner2, planner10 자연정렬 + 한글 ㄱ~ㅎ
    private int compareNameNatural(String nameA, String nameB) {
        if (nameA == null && nameB == null) return 0;
        if (nameA == null) return 1;
        if (nameB == null) return -1;

        String textA = nameA.replaceAll("\\d", "");
        String textB = nameB.replaceAll("\\d", "");

        int textCompare = textA.compareTo(textB);
        if (textCompare != 0) return textCompare;

        String numA = nameA.replaceAll("\\D", "");
        String numB = nameB.replaceAll("\\D", "");

        if (numA.isEmpty() || numB.isEmpty()) {
            return nameA.compareTo(nameB);
        }

        int intA = Integer.parseInt(numA);
        int intB = Integer.parseInt(numB);

        return Integer.compare(intA, intB);
    }

    // GET	/api/member/{memberId}	계정 조회	본인
    public MemberSearchByIdResponse memberSearchByIdService(Member member, String memberId) {
        String loginId = member.getId();

        if (!loginId.equals(memberId)) {
            throw new SecurityException("본인 정보만 조회할 수 있습니다.");
        }

        Member self = memberRepository.findById(member.getId())
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 멤버입니다."));

        if (Boolean.FALSE.equals(self.getIsActive())) {
            throw new SecurityException("비활성 계정입니다.");
        }

        return MemberSearchByIdResponse.builder().member(self).build();
    }


}
