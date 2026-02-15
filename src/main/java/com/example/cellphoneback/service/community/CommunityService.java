
package com.example.cellphoneback.service.community;

import com.example.cellphoneback.dto.request.community.CreateCommunityRequest;
import com.example.cellphoneback.dto.request.community.EditCommunityRequest;
import com.example.cellphoneback.dto.response.community.CommunityListResponse;
import com.example.cellphoneback.dto.response.community.SearchAllCommunityResponse;
import com.example.cellphoneback.dto.response.community.SearchCommunityByIdResponse;
import com.example.cellphoneback.entity.community.Community;
import com.example.cellphoneback.entity.member.Member;
import com.example.cellphoneback.entity.member.Role;
import com.example.cellphoneback.repository.community.CommentRepository;
import com.example.cellphoneback.repository.community.CommunityRepository;
import com.example.cellphoneback.repository.notice.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class
CommunityService {
    final CommunityRepository communityRepository;
    final CommentRepository commentRepository;

    //    community	POST	/api/community	게시글 작성	planner, worker
    public Community createCommunity(Member member, CreateCommunityRequest request) {

        if (!member.getRole().equals(Role.PLANNER) && !member.getRole().equals(Role.WORKER)) {
            throw new SecurityException("PLANNER, WORKER 권한이 없습니다.");
        }

        Community community = request.toEntity();
        community.setMember(member);
        communityRepository.save(community);

        return community;
    }

    //    community	PUT	/api/community	게시글 수정	planner, worker
    @Transactional
    public Community editCommunity(Member member, Integer communityId, EditCommunityRequest request) {

        if (!member.getRole().equals(Role.PLANNER) && !member.getRole().equals(Role.WORKER)) {
            throw new SecurityException("PLANNER, WORKER 권한이 없습니다.");
        }

        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        if(!community.getMember().getId().equals(member.getId())) {
            throw new SecurityException("작성자만 수정 가능합니다.");
        }

        community.setTitle(request.getTitle());
        community.setContent(request.getContent());

        return communityRepository.save(community);
    }

    //    community	DELETE	/api/community	게시글 삭제	planner, worker
    public void deleteCommunity(Member member, Integer communityId) {

        if (!member.getRole().equals(Role.PLANNER) && !member.getRole().equals(Role.WORKER)) {
            throw new SecurityException("PLANNER, WORKER 권한이 없습니다.");
        }

        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        if (!community.getMember().getId().equals(member.getId())) {
            throw new SecurityException("작성자만 삭제 가능합니다.");
        }

        communityRepository.delete(community);
    }

    //    community	GET	/api/community	게시글 전체 조회	all
    public SearchAllCommunityResponse searchAllCommunity(String keyword) {

        List<Community> communities = communityRepository.findAll();

        if (communities.isEmpty()) {
            return SearchAllCommunityResponse.builder()
                    .totalCount(0)
                    .communityList(List.of())
                    .build();
        }

        // 정렬 - 최신순/검색
        List<CommunityListResponse> communityList = communities.stream()
                // 커뮤니티 createdAt 기준 정렬하고 null 값이 있으면 맨뒤로 보내고 있으면 날짜순으로 비교해서 최신순 정렬함
                .sorted(Comparator.comparing(Community::getCreatedAt, Comparator.nullsLast(Comparator.naturalOrder())).reversed())
                .filter(c -> {
                    if (keyword == null || keyword.isBlank())
                        return true;

                    String kw = keyword.trim().toLowerCase().replaceAll("\\s+", "");
                    return (c.getTitle() != null && c.getTitle().toLowerCase().replaceAll("\\s+", "").contains(kw)) ||
                            (c.getContent() != null && c.getContent().toLowerCase().replaceAll("\\s+", "").contains(kw));
                })
                .map(CommunityListResponse::fromEntity)
                .toList();

        long totalCommunityCount = communityList.size();

        return SearchAllCommunityResponse.builder()
                .totalCount(totalCommunityCount)
                .communityList(communityList)
                .build();
    }

    // community	GET	/api/community/{communityId}	해당 게시글 조회	all
    @Transactional
    public SearchCommunityByIdResponse searchCommunityById(Integer communityId) {

        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        communityRepository.increaseViewCount(communityId);

        return SearchCommunityByIdResponse.fromEntity(community);
    }
}
