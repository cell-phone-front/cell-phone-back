
package com.example.cellphoneback.service.community;

import com.example.cellphoneback.dto.request.community.CreateCommentRequest;
import com.example.cellphoneback.dto.request.community.EditCommentRequest;
import com.example.cellphoneback.dto.response.community.comment.CreateCommentResponse;
import com.example.cellphoneback.dto.response.community.comment.EditCommentResponse;
import com.example.cellphoneback.dto.response.community.comment.SearchAllCommentResponse;
import com.example.cellphoneback.entity.community.Comment;
import com.example.cellphoneback.entity.community.Community;
import com.example.cellphoneback.entity.member.Member;
import com.example.cellphoneback.entity.member.Role;
import com.example.cellphoneback.repository.community.CommentRepository;
import com.example.cellphoneback.repository.community.CommunityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommunityRepository communityRepository;
    private final CommentRepository commentRepository;

    //    community	POST	/api/comment/{communityId}/	댓글 작성	planner, worker	pathvariable={communityId}
    @Transactional
    public CreateCommentResponse createComment(Member member, Integer communityId, CreateCommentRequest request) {

        if (!member.getRole().equals(Role.PLANNER) && !member.getRole().equals(Role.WORKER)) {
            throw new SecurityException("댓글 작성 권한이 없습니다.");
        }

        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글"));

        Comment comment = request.toEntity();
        comment.setMember(member);
        comment.setCommunity(community);

        Comment save = commentRepository.save(comment);

        community.setCommentCount(community.getCommentCount() + 1);
        communityRepository.save(community);

        // 익명 번호 계산
        String anonymous = anonymousName(communityId, member.getId());

        return CreateCommentResponse.fromEntity(save, anonymous);
    }
    // 익명번호 로직
    // 같은 커뮤니티 내에서 등장 순서대로 익명 1/2/3 부여
    // 같은 작성자는 같은 번호 유지
    private String anonymousName(Integer communityId, String memberId) {
        List<Comment> comments = commentRepository.findByCommunityId(communityId);

        Map<String, Integer> map = new HashMap<>();
        int seq = 1;

        for(Comment c : comments) {
            String id = c.getMember().getId();
            if(!map.containsKey(id)) {
                map.put(id, seq++);
            }
        }
        // 작성 직후라 대부분 포함되지만 없으면 새 번호
        if(!map.containsKey(memberId)) {
            map.put(memberId, seq);
        }
        return "익명" + map.get(memberId);
    }

    //    community	PUT	/api/comment/{communityId}/	댓글 수정	planner, worker	pathvariable={communityId}
    public EditCommentResponse editComment(Integer communityId, Integer commentId, Member member, EditCommentRequest request) {

        if (!member.getRole().equals(Role.PLANNER) && !member.getRole().equals(Role.WORKER)) {
            throw new SecurityException("PLANNER, WORKER 권한이 없습니다.");
        }

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 commentId입니다."));

        if(!Objects.equals(comment.getCommunity().getId(), communityId)) {
            throw  new IllegalArgumentException("해당 게시글에 속하지 않는 댓글입니다.");
        }

        if (!comment.getMember().getId().equals(member.getId())) {
            throw new SecurityException("작성자만 수정 가능합니다.");
        }

        comment.setContent(request.getContent());

        Comment save = commentRepository.save(comment);

        String anonymous = anonymousName(communityId, save.getMember().getId());

        return EditCommentResponse.fromEntity(save, anonymous);
    }

    //    community	DELETE	/api/comment/{communityId}/{commentId}	댓글 삭제	planner, worker	pathvariable={communityId}
    public void deleteComment(Integer communityId, Integer commentId, Member member) {

        if (!member.getRole().equals(Role.PLANNER) && !member.getRole().equals(Role.WORKER)) {
            throw new SecurityException("PLANNER, WORKER 권한이 없습니다.");
        }

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 commentId입니다."));

        if(!Objects.equals(comment.getCommunity().getId(), communityId)) {
            throw new IllegalArgumentException("해당 게시글에 속하지 않는 댓글입니다.");
        }

        if (!comment.getMember().getId().equals(member.getId())) {
            throw new SecurityException("작성자만 삭제 가능합니다.");
        }

        Community community = comment.getCommunity();
        commentRepository.delete(comment);

        int next = Math.max(0, community.getCommentCount() - 1);
        community.setCommentCount(next);
        communityRepository.save(community);
    }

    //    community	GET	/api/comment/{communityId}/	댓글  조회	all	pathvariable={communityId}
    public List<SearchAllCommentResponse> searchAllComment(Integer communityId) {

        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        List<Comment> comments = commentRepository.findByCommunityId(communityId);

        Map<String, Integer> map = new HashMap<>();
        int seq = 1;

        List<SearchAllCommentResponse> result = new ArrayList<>();

        for(Comment c : comments) {
            String memberId = c.getMember().getId();

            if(!map.containsKey(memberId)) {
                map.put(memberId, seq++);
            }

            String anonymous = "익명" + map.get(memberId);

            result.add(SearchAllCommentResponse.fromEntity(c, anonymous));
        }

        return result;
    }

}
