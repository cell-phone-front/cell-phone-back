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
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

//@SecurityRequirement(name = "bearerAuth")
//@Tag(name = "Comment", description = "댓글 관련 API")
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {
    private final CommentService commentService;

    // POST	/api/comment/{communityId}/	댓글 작성	planner, worker	pathvariable={communityId}
    @PostMapping("/{communityId}")
    public ResponseEntity<CreateCommentResponse> createComment(@PathVariable Integer communityId,
                                                               @RequestAttribute Member member,
                                                               @RequestBody CreateCommentRequest request) {

        Comment response = commentService.createComment(member, communityId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED) //201
                .body(CreateCommentResponse.fromEntity(response));
    }

    // PUT	/api/comment/{communityId}/	댓글 수정	planner, worker	pathvariable={communityId}
    @PutMapping("/{commentId}")
    public ResponseEntity<EditCommentResponse> updateComment(@PathVariable Integer commentId,
                                                             @RequestAttribute Member member,
                                                             @RequestBody EditCommentRequest request) {
        Comment response = commentService.editComment(member, commentId, request);
        return ResponseEntity
                .status(HttpStatus.OK) //200
                .body(EditCommentResponse.fromEntity(response));
    }

    //DELETE	/api/comment/{communityId}/	댓글 삭제	planner, worker	pathvariable={communityId}
    @DeleteMapping("/{commentId}")
    public ResponseEntity<DeleteCommentResponse> deleteComment(@PathVariable Integer commentId,
                                                               @RequestAttribute Member member) {
        commentService.deleteComment(member, commentId);

        return ResponseEntity
                .status(HttpStatus.OK) //200
                .body(DeleteCommentResponse.fromEntity());
    }

    // GET	/api/comment/{communityId}/	댓글  조회	all	pathvariable={communityId}
    @GetMapping("/{communityId}")
    public ResponseEntity<List<SearchAllCommentResponse>> getComments(@PathVariable Integer communityId) {
        List<Comment> response = commentService.searchAllComment(communityId);

        List<SearchAllCommentResponse> responseList = response.stream()
                .map(SearchAllCommentResponse::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity
                .status(HttpStatus.OK) //200
                .body(responseList);
    }
}
