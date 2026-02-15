package com.example.cellphoneback.service.dashboard;

import com.example.cellphoneback.dto.response.dashboard.DashboardSearchResponse;
import com.example.cellphoneback.dto.response.community.SearchAllCommunityResponse;
import com.example.cellphoneback.dto.response.member.MemberListResponse;
import com.example.cellphoneback.dto.response.notice.SearchAllNoticeResponse;
import com.example.cellphoneback.dto.response.operation.machine.MachineListResponse;
import com.example.cellphoneback.dto.response.operation.operation.OperationListResponse;
import com.example.cellphoneback.dto.response.operation.product.ProductListResponse;
import com.example.cellphoneback.dto.response.operation.productRouting.ProductRoutingListResponse;
import com.example.cellphoneback.dto.response.operation.task.TaskListResponse;
import com.example.cellphoneback.dto.response.simulation.GetAllSimulationResponse;
import com.example.cellphoneback.entity.member.Member;
import com.example.cellphoneback.entity.member.Role;
import com.example.cellphoneback.service.community.CommunityService;
import com.example.cellphoneback.service.member.MemberService;
import com.example.cellphoneback.service.notice.NoticeService;
import com.example.cellphoneback.service.operation.*;
import com.example.cellphoneback.service.simulation.SimulationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Supplier;

@RequiredArgsConstructor
@Service
public class DashboardService {

    private final CommunityService communityService;
    private final MemberService memberService;
    private final NoticeService noticeService;
    private final MachineService machineService;
    private final OperationService operationService;
    private final ProductService productService;
    private final ProductRoutingService productRoutingService;
    private final TaskService taskService;
    private final SimulationService simulationService;

    public DashboardSearchResponse dashboardSearchAll(Member member, String keyword) {

        if (member.getRole() == Role.ADMIN || member.getRole() == Role.PLANNER ) {

            if (keyword == null || keyword.isBlank()) {
                return DashboardSearchResponse.empty();
            }

            SearchAllCommunityResponse communities = safeCall(
                    () -> communityService.searchAllCommunity(keyword),
                    SearchAllCommunityResponse.builder()
                            .totalCount(0L)
                            .communityList(List.of())
                            .build()
            );

            SearchAllNoticeResponse notices = safeCall(
                    () -> noticeService.searchAllNotice(keyword),
                    SearchAllNoticeResponse.builder()
                            .noticeList(List.of())
                            .build()
            );

            MachineListResponse machines = safeCall(
                    () -> machineService.machineListService(member, keyword),
                    MachineListResponse.builder()
                            .machineList(List.of())
                            .build()
            );

            OperationListResponse operations = safeCall(
                    () -> operationService.operationListService(member, keyword),
                    OperationListResponse.builder()
                            .operationList(List.of())
                            .build()
            );

            ProductListResponse products = safeCall(
                    () -> productService.productListService(member, keyword),
                    ProductListResponse.builder()
                            .productList(List.of())
                            .build()
            );

            ProductRoutingListResponse productRoutingList = safeCall(
                    () -> productRoutingService.productRoutingListService(member, keyword),
                    ProductRoutingListResponse.builder()
                            .productRoutingList(List.of())
                            .build()
            );

            TaskListResponse tasks = safeCall(
                    () -> taskService.taskListService(member, keyword),
                    TaskListResponse.builder()
                            .taskList(List.of())
                            .build()
            );

            GetAllSimulationResponse simulations = safeCall(
                    () -> simulationService.getAllSimulations(member, keyword),
                    GetAllSimulationResponse.builder()
                            .simulationList(List.of())
                            .build()
            );

            return DashboardSearchResponse.builder()
                    .communities(communities)
                    .notices(notices)
                    .machines(machines)
                    .operations(operations)
                    .products(products)
                    .productRouting(productRoutingList)
                    .tasks(tasks)
                    .simulations(simulations)
                    .build();

        } else if(member.getRole() == Role.WORKER) {

            if (keyword == null || keyword.isBlank()) {
                return DashboardSearchResponse.empty();
            }

            SearchAllCommunityResponse communities = safeCall(
                    () -> communityService.searchAllCommunity(keyword),
                    SearchAllCommunityResponse.builder()
                            .totalCount(0L)
                            .communityList(List.of())
                            .build()
            );

            SearchAllNoticeResponse notices = safeCall(
                    () -> noticeService.searchAllNotice(keyword),
                    SearchAllNoticeResponse.builder()
                            .noticeList(List.of())
                            .build()
            );

            return DashboardSearchResponse.builder()
                    .communities(communities)
                    .notices(notices)
                    .build();
        }

        return DashboardSearchResponse.empty();
    }

    private <T> T safeCall(Supplier<T> supplier, T emptyValue) {
        try {
            return supplier.get();
        } catch (Exception e) {
            return emptyValue;
        }
    }
}