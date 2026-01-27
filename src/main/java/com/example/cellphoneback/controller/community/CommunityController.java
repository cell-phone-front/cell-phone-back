package com.example.cellphoneback.controller.community;

import com.example.cellphoneback.dto.request.community.CreateCommunityRequest;
import com.example.cellphoneback.dto.request.community.EditCommunityRequest;
import com.example.cellphoneback.dto.response.community.*;
import com.example.cellphoneback.entity.community.Community;
import com.example.cellphoneback.entity.member.Member;
import com.example.cellphoneback.service.community.CommunityService;
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
@RequestMapping("/api/community")
public class CommunityController {
    final CommunityService communityService;

    //    community	POST	/api/community	게시글 작성	planner, worker
    @PostMapping
    public ResponseEntity<CreateCommunityResponse> createCommunity(@RequestAttribute Member member,
                                                                   @RequestBody CreateCommunityRequest request) {

        Community response = communityService.createCommunity(member, request);

        return ResponseEntity
                .status(HttpStatus.CREATED) //201
                .body(CreateCommunityResponse.fromEntity(response));

    }

    //    community	PUT	/api/community	게시글 수정	planner, worker
    @PutMapping("/{communityId}")
    public ResponseEntity<EditCommunityResponse> editCommunity(@RequestAttribute Member member,
                                                               @PathVariable Integer communityId,
                                                               @RequestBody EditCommunityRequest request) {

        Community response = communityService.editCommunity(member, communityId, request);
        return ResponseEntity
                .status(HttpStatus.OK) //201
                .body(EditCommunityResponse.fromEntity(response));
    }

    //    community	DELETE	/api/community	게시글 삭제	planner, worker
    @DeleteMapping("/{communityId}")
    public ResponseEntity<DeleteCommunityResponse> deleteCommunity(@RequestAttribute Member member,
                                                                    @PathVariable Integer communityId) {

        communityService.deleteCommunity(member, communityId);

        return ResponseEntity
                .status(HttpStatus.OK) //201
                .body(DeleteCommunityResponse.fromEntity());
    }

    //    community	GET	/api/community	게시글 조회	all
    @GetMapping
    public ResponseEntity<List<SearchAllCommunityResponse>> searchAllCommunity(@RequestParam(required = false) String keyword) {

        List<Community> response = communityService.searchAllCommunity(keyword);

        List<SearchAllCommunityResponse> responseList = response.stream()
                .map(SearchAllCommunityResponse::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity
                .status(HttpStatus.OK) //200
                .body(responseList);
    }

    // community	GET	/api/community/{communityId}	해당 게시글 조회	all
    @GetMapping("/{communityId}")
    public ResponseEntity<SearchCommunityByIdResponse> searchCommunityById(@PathVariable Integer communityId) {

        Community response = communityService.searchCommunityById(communityId);

        return ResponseEntity
                .status(HttpStatus.OK) //201
                .body(SearchCommunityByIdResponse.fromEntity(response));
    }

    // community	GET	/api/community/{communityId}/comment-count	댓글 수 조회	all	pathvariable={commentId}
    @GetMapping("/{communityId}/comment-count")
    public ResponseEntity<CommentCountResponse> commentCount(@PathVariable Integer communityId) {

        Community response = communityService.commentCount(communityId);

        return ResponseEntity
                .status(HttpStatus.OK) //201
                .body(CommentCountResponse.fromEntity(response));
    }
}
