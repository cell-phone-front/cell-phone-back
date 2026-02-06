package com.example.cellphoneback.controller.community;

import com.example.cellphoneback.dto.request.community.CreateCommunityRequest;
import com.example.cellphoneback.dto.request.community.EditCommunityRequest;
import com.example.cellphoneback.dto.response.community.*;
import com.example.cellphoneback.entity.community.Community;
import com.example.cellphoneback.entity.member.Member;
import com.example.cellphoneback.service.community.CommunityService;
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
@Tag(name = "Community", description = "커뮤니티 관련 API")
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/community")
public class CommunityController {
    final CommunityService communityService;

    @Operation(summary = "게시글 작성", description = "커뮤니티에 새 게시글을 작성합니다.")
    @PostMapping
    public ResponseEntity<CreateCommunityResponse> createCommunity(@RequestAttribute Member member,
                                                                   @RequestBody CreateCommunityRequest request) {

        Community response = communityService.createCommunity(member, request);

        return ResponseEntity
                .status(HttpStatus.CREATED) //201
                .body(CreateCommunityResponse.fromEntity(response));

    }

    @Operation(summary = "게시글 수정", description = "커뮤니티에 작성된 게시글을 수정합니다.")
    @PutMapping("/{communityId}")
    public ResponseEntity<EditCommunityResponse> editCommunity(@RequestAttribute Member member,
                                                               @PathVariable Integer communityId,
                                                               @RequestBody EditCommunityRequest request) {

        Community response = communityService.editCommunity(member, communityId, request);
        return ResponseEntity
                .status(HttpStatus.OK) //201
                .body(EditCommunityResponse.fromEntity(response));
    }

    @Operation(summary = "게시글 삭제", description = "커뮤니티에 작성된 게시글을 삭제합니다.")
    @DeleteMapping("/{communityId}")
    public ResponseEntity<DeleteCommunityResponse> deleteCommunity(@RequestAttribute Member member,
                                                                    @PathVariable Integer communityId) {

        communityService.deleteCommunity(member, communityId);

        return ResponseEntity
                .status(HttpStatus.OK) //201
                .body(DeleteCommunityResponse.fromEntity());
    }

    @Operation(summary = "게시글 전체 목록 조회", description = "커뮤니티에 작성된 모든 게시글을 조회합니다.")
    @GetMapping
    public ResponseEntity<SearchAllCommunityResponse> searchAllCommunity(@RequestParam(required = false) String keyword) {

        SearchAllCommunityResponse response = communityService.searchAllCommunity(keyword);

        return ResponseEntity
                .status(HttpStatus.OK) //200
                .body(response);
    }

    @Operation(summary = "게시글 상세 조회", description = "커뮤니티에 작성된 게시글을 상세 조회합니다.")
    @GetMapping("/{communityId}")
    public ResponseEntity<SearchCommunityByIdResponse> searchCommunityById(@PathVariable Integer communityId) {

        Community response = communityService.searchCommunityById(communityId);

        return ResponseEntity
                .status(HttpStatus.OK) //201
                .body(SearchCommunityByIdResponse.fromEntity(response));
    }
}
