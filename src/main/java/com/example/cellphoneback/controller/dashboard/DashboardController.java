package com.example.cellphoneback.controller.dashboard;

import com.example.cellphoneback.dto.response.dashboard.DashboardSearchResponse;
import com.example.cellphoneback.entity.member.Member;
import com.example.cellphoneback.service.dashboard.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

//@SecurityRequirement(name = "bearerAuth")
//@Tag(name = "Comment", description = "댓글 관련 API")
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping("/search")
    public DashboardSearchResponse dashboardSearch(@RequestAttribute Member member,
                                                   @RequestParam(required = false) String keyword) {
        return dashboardService.dashboardSearchAll(member, keyword);
    }

}
