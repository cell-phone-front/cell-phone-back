package com.example.cellphoneback.controller.dashboard;

import com.example.cellphoneback.dto.response.dashboard.DashboardSearchResponse;
import com.example.cellphoneback.entity.member.Member;
import com.example.cellphoneback.service.dashboard.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Dashboard", description = "대시보드 관련 API")
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;

    @Operation(summary = "대시보드 통합 검색", description = "대시보드에서 통합 검색을 수행합니다.")
    @GetMapping("/search")
    public DashboardSearchResponse dashboardSearch(@RequestAttribute Member member,
                                                   @RequestParam(required = false) String keyword) {
        return dashboardService.dashboardSearchAll(member, keyword);
    }

}
