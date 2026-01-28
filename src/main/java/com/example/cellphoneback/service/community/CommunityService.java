
package com.example.cellphoneback.service.community;

import com.example.cellphoneback.dto.request.community.CreateCommunityRequest;
import com.example.cellphoneback.dto.request.community.EditCommunityRequest;
import com.example.cellphoneback.entity.community.Community;
import com.example.cellphoneback.entity.member.Member;
import com.example.cellphoneback.entity.member.Role;
import com.example.cellphoneback.repository.community.CommentRepository;
import com.example.cellphoneback.repository.community.CommunityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

        return communityRepository.save(community);
    }

    //    community	PUT	/api/community	게시글 수정	planner, worker
    public Community editCommunity(Member member, Integer communityId, EditCommunityRequest request) {

        if (!member.getRole().equals(Role.PLANNER) && !member.getRole().equals(Role.WORKER)) {
            throw new SecurityException("게시글 수정 권한이 없습니다.");
        }
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));


        community.setTitle(request.getTitle());
        community.setDescription(request.getDescription());

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
    public List<Community> searchAllCommunity(String keyword) {

        List<Community> communities = communityRepository.findAll();

        if (communities.isEmpty()) {
            throw new IllegalArgumentException("존재하는 게시글이 없습니다.");
        }

        // 정렬 - 최신순/검색
        List<Community> communityList = communities.stream()
                .sorted(Comparator.comparing(Community::getCreatedAt).reversed())
                .filter(c -> {
                    if (keyword == null || keyword.isBlank())
                        return true;

                    String kw = keyword.trim();
                    return (c.getTitle() != null && c.getTitle().contains(kw)) ||
                            (c.getDescription() != null && c.getDescription().contains(kw));
                })
                .toList();


        // 각 글마다 댓글 수 설정
        for (Community community : communityList) {
            long commentCount = commentRepository.countByCommunityId(community.getId());
            community.setCommentCount((int) commentCount);
        }

        return communityList;
    }

    // community	GET	/api/community/{communityId}	해당 게시글 조회	all
    public Community searchCommunityById(Integer communityId) {

        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));


        return community;
    }

    // community	GET	/api/community/{communityId}/comment-count	댓글 수 조회	all
    public Community commentCount(Integer communityId) {

        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        community.setCommentCount(community.getCommentCount() + 1);

        return community;

    }
}
