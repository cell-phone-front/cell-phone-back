
package com.example.cellphoneback.service.community;

import com.example.cellphoneback.dto.request.community.CreateCommentRequest;
import com.example.cellphoneback.dto.request.community.EditCommentRequest;
import com.example.cellphoneback.dto.request.notice.EditNoticeRequest;
import com.example.cellphoneback.entity.community.Comment;
import com.example.cellphoneback.entity.community.Community;
import com.example.cellphoneback.entity.member.Member;
import com.example.cellphoneback.entity.member.Role;
import com.example.cellphoneback.repository.community.CommentRepository;
import com.example.cellphoneback.repository.community.CommunityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommunityRepository communityRepository;
    private final CommentRepository commentRepository;

    //    community	POST	/api/comment/{communityId}/	댓글 작성	planner, worker	pathvariable={communityId}
    public Comment createComment(Member member, Integer communityId, CreateCommentRequest request) {

        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글"));

        if (!member.getRole().equals(Role.PLANNER) && !member.getRole().equals(Role.WORKER)) {
            throw new SecurityException("게시글 작성 권한이 없습니다.");
        }

        Comment comment = request.toEntity();
        comment.setMember(member);
        comment.setCommunity(community);

        return commentRepository.save(comment);
    }

    //    community	PUT	/api/comment/{communityId}/	댓글 수정	planner, worker	pathvariable={communityId}
    public Comment editComment(Member member, Integer commentId, EditCommentRequest request) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        if (!member.getRole().equals(Role.PLANNER) && !member.getRole().equals(Role.WORKER)) {
            throw new SecurityException("게시글 수정 권한이 없습니다.");
        }

        comment.setContent(request.getContent());

        return commentRepository.save(comment);
    }

    //    community	DELETE	/api/comment/{communityId}/	댓글 삭제	planner, worker	pathvariable={communityId}
    public void deleteComment(Member member, Integer commentId) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));


        if (!member.getRole().equals(Role.PLANNER) && !member.getRole().equals(Role.WORKER)) {
            throw new SecurityException("게시글 삭제 권한이 없습니다.");
        }

        commentRepository.delete(comment);
    }

    //    community	GET	/api/comment/{communityId}/	댓글  조회	all	pathvariable={communityId}
    public List<Comment> searchAllComment(Integer communityId) {

        communityRepository.findById(communityId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));


        return commentRepository.findByCommunityId(communityId);
    }

}
