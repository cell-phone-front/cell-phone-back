package com.example.cellphoneback.controller.community;

import com.example.cellphoneback.dto.request.community.CreateCommentRequest;
import com.example.cellphoneback.dto.request.community.EditCommentRequest;
import com.example.cellphoneback.dto.response.community.comment.CreateCommentResponse;
import com.example.cellphoneback.dto.response.community.comment.DeleteCommentResponse;
import com.example.cellphoneback.dto.response.community.comment.EditCommentResponse;
import com.example.cellphoneback.dto.response.community.comment.SearchAllCommentResponse;
import com.example.cellphoneback.entity.community.Comment;
import com.example.cellphoneback.entity.member.Member;
import com.example.cellphoneback.service.community.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Comment", description = "커뮤니티 댓글 관련 API")
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {
    private final CommentService commentService;

    @Operation(summary = "댓글 작성", description = "커뮤니티 글에 새 댓글을 작성합니다. 기획자(PLANNER), 작업자(WORKER) 권한만 접근 가능합니다.")
    @PostMapping("/{communityId}")
    public ResponseEntity<CreateCommentResponse> createComment(@PathVariable Integer communityId,
                                                               @RequestAttribute Member member,
                                                               @RequestBody CreateCommentRequest request) {

        CreateCommentResponse response = commentService.createComment(member, communityId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED) //201
                .body(response);
    }

    @Operation(summary = "댓글 수정", description = "커뮤니티에 작성된 댓글을 수정합니다. 작성자만 접근할 수 있습니다.")
    @PutMapping("/{communityId}/{commentId}")
    public ResponseEntity<EditCommentResponse> updateComment(@PathVariable Integer communityId,
                                                             @PathVariable Integer commentId,
                                                             @RequestAttribute Member member,
                                                             @RequestBody EditCommentRequest request) {
        EditCommentResponse response = commentService.editComment(communityId, commentId, member, request);
        return ResponseEntity
                .status(HttpStatus.OK) //200
                .body(response);
    }

    @Operation(summary = "댓글 삭제", description = "커뮤니티에 작성된 댓글을 삭제합니다. 작성자만 접근할 수 있습니다.")
    @DeleteMapping("/{communityId}/{commentId}")
    public ResponseEntity<DeleteCommentResponse> deleteComment(@PathVariable Integer communityId,
                                                               @PathVariable Integer commentId,
                                                               @RequestAttribute Member member) {
        commentService.deleteComment(communityId, commentId, member);

        return ResponseEntity
                .status(HttpStatus.OK) //200
                .body(DeleteCommentResponse.fromEntity());
    }

    @Operation(summary = "댓글 전체 목록 조회", description = "커뮤니티 글에 작성된 모든 댓글을 조회합니다. 모든 사용자가 접근할 수 있습니다.")
    @GetMapping("/{communityId}")
    public ResponseEntity<List<SearchAllCommentResponse>> getComments(@PathVariable Integer communityId) {

        List<SearchAllCommentResponse> response = commentService.searchAllComment(communityId);

        return ResponseEntity
                .status(HttpStatus.OK) //200
                .body(response);
    }
}
