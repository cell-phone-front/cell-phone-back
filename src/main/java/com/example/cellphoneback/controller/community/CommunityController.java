package com.example.cellphoneback.controller.community;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@SecurityRequirement(name = "bearerAuth")
//@Tag(name = "Comment", description = "댓글 관련 API")
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/community")
public class CommunityController {

//    community	POST	/api/community	게시글 작성	planner, worker
//    community	PUT	/api/community	게시글 수정	planner, worker
//    community	DELETE	/api/community	게시글 삭제	planner, worker
//    community	GET	/api/community	게시글 조회	all
}
