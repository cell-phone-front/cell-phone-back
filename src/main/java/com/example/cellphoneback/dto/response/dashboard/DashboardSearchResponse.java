package com.example.cellphoneback.dto.response.dashboard;

import com.example.cellphoneback.dto.response.community.SearchAllCommunityResponse;
import com.example.cellphoneback.dto.response.member.MemberListResponse;
import com.example.cellphoneback.dto.response.notice.SearchAllNoticeResponse;
import com.example.cellphoneback.dto.response.operation.machine.MachineListResponse;
import com.example.cellphoneback.dto.response.operation.operation.OperationListResponse;
import com.example.cellphoneback.dto.response.operation.product.ProductListResponse;
import com.example.cellphoneback.dto.response.operation.task.TaskListResponse;
import com.example.cellphoneback.dto.response.simulation.GetAllSimulationResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "대시보드 전체 검색 결과 DTO")
public class DashboardSearchResponse {

    @Schema(description = "커뮤니티 검색 결과")
    private SearchAllCommunityResponse communities;

    @Schema(description = "회원 리스트")
    private MemberListResponse members;

    @Schema(description = "공지사항 리스트")
    private SearchAllNoticeResponse notices;

    @Schema(description = "기계 리스트")
    private MachineListResponse machines;

    @Schema(description = "작업(Operation) 리스트")
    private OperationListResponse operations;

    @Schema(description = "상품 리스트")
    private ProductListResponse products;

    @Schema(description = "태스크 리스트")
    private TaskListResponse tasks;

    @Schema(description = "시뮬레이션 스케줄 리스트")
    private GetAllSimulationResponse simulations;

    public static DashboardSearchResponse empty() {
        return DashboardSearchResponse.builder()
                .communities(SearchAllCommunityResponse.builder()
                        .totalCount(0L)
                        .communityList(java.util.List.of())
                        .build())
                .members(MemberListResponse.builder()
                        .memberList(java.util.List.of())
                        .build())
                .notices(SearchAllNoticeResponse.builder()
                        .noticeList(java.util.List.of())
                        .build())
                .machines(MachineListResponse.builder()
                        .machineList(java.util.List.of())
                        .build())
                .operations(OperationListResponse.builder()
                        .operationList(java.util.List.of())
                        .build())
                .products(ProductListResponse.builder()
                        .productList(java.util.List.of())
                        .build())
                .tasks(TaskListResponse.builder()
                        .taskList(java.util.List.of())
                        .build())
                .simulations(GetAllSimulationResponse.builder()
                        .simulationScheduleList(java.util.List.of())
                        .build())
                .build();
    }
}