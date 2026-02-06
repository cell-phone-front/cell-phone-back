package com.example.cellphoneback.dto.response.dashboard;

import com.example.cellphoneback.dto.response.community.SearchAllCommunityResponse;
import com.example.cellphoneback.dto.response.member.MemberListResponse;
import com.example.cellphoneback.dto.response.notice.SearchAllNoticeResponse;
import com.example.cellphoneback.dto.response.operation.machine.MachineListResponse;
import com.example.cellphoneback.dto.response.operation.operation.OperationListResponse;
import com.example.cellphoneback.dto.response.operation.product.ProductListResponse;
import com.example.cellphoneback.dto.response.operation.task.TaskListResponse;
import com.example.cellphoneback.dto.response.simulation.GetAllSimulationResponse;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DashboardSearchResponse {

    private SearchAllCommunityResponse communities;
    private MemberListResponse members;
    private SearchAllNoticeResponse notices;
    private MachineListResponse machines;
    private OperationListResponse operations;
    private ProductListResponse products;
    private TaskListResponse tasks;
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