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
public class CommentController {

//    community	POST	/api/community/{communityId}	댓글 작성	planner, worker
//    community	PUT	/api/community/{communityId}	댓글 수정	planner, worker
//    community	DELETE	/api/community/{communityId}	댓글 삭제	planner, worker
//    community	GET	/api/community/{communityId}	댓글 조회	all
}
