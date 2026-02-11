
package com.example.cellphoneback.service.community;

import com.example.cellphoneback.dto.request.community.CreateCommunityRequest;
import com.example.cellphoneback.dto.request.community.EditCommunityRequest;
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
public class CommunityService {
    final CommunityRepository communityRepository;
    final CommentRepository commentRepository;

    //    community	POST	/api/community	게시글 작성	planner, worker
    public Community createCommunity(Member member, CreateCommunityRequest request) {

        if (!member.getRole().equals(Role.PLANNER) && !member.getRole().equals(Role.WORKER)) {
            throw new SecurityException("게시글 작성 권한이 없습니다.");
        }

        Community community = request.toEntity();
        community.setMember(member);
        community.setCommentCount(0);
        communityRepository.save(community);

        return community;
    }

    //    community	PUT	/api/community	게시글 수정	planner, worker
    public Community editCommunity(Member member, Integer communityId, EditCommunityRequest request) {

        if (!member.getRole().equals(Role.PLANNER) && !member.getRole().equals(Role.WORKER)) {
            throw new SecurityException("게시글 수정 권한이 없습니다.");
        }
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));


        community.setTitle(request.getTitle());
        community.setContent(request.getContent());

        return communityRepository.save(community);
    }

    //    community	DELETE	/api/community	게시글 삭제	planner, worker
    public void deleteCommunity(Member member, Integer communityId) {

        if (!member.getRole().equals(Role.PLANNER) && !member.getRole().equals(Role.WORKER)) {
            throw new SecurityException("게시글 삭제 권한이 없습니다.");
        }

        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));


        communityRepository.delete(community);
    }

    //    community	GET	/api/community	게시글 전체 조회	all
    public SearchAllCommunityResponse searchAllCommunity(String keyword) {

        long totalCommunityCount = communityRepository.count();

        List<Community> communities = communityRepository.findAll();

        if (communities.isEmpty()) {
            throw new IllegalArgumentException("존재하는 게시글이 없습니다.");
        }

        // 정렬 - 최신순/검색
        List<SearchCommunityByIdResponse> communityList = communities.stream()
                .sorted(Comparator.comparing(Community::getCreatedAt).reversed())
                .filter(c -> {
                    if (keyword == null || keyword.isBlank())
                        return true;

                    String kw = keyword.trim().toLowerCase().replaceAll("\\s+", "");
                    return (c.getTitle() != null && c.getTitle().toLowerCase().replaceAll("\\s+", "").contains(kw)) ||
                            (c.getContent() != null && c.getContent().toLowerCase().replaceAll("\\s+", "").contains(kw));
                })
                .map(SearchCommunityByIdResponse::fromEntity)
                .toList();

        return SearchAllCommunityResponse.builder()
                .totalCount(totalCommunityCount)
                .communityList(communityList)
                .build();
    }

    // community	GET	/api/community/{communityId}	해당 게시글 조회	all
    @Transactional
    public Community searchCommunityById(Integer communityId) {

        communityRepository.increaseViewCount(communityId);

        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));


        return community;
    }
}
