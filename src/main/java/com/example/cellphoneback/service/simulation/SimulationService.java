
package com.example.cellphoneback.service.simulation;

import com.example.cellphoneback.dto.request.simulation.CreateSimulationRequest;
import com.example.cellphoneback.dto.response.simulation.*;
import com.example.cellphoneback.entity.member.Member;
import com.example.cellphoneback.entity.member.Role;
import com.example.cellphoneback.entity.simulation.Simulation;
import com.example.cellphoneback.entity.simulation.SimulationProduct;
import com.example.cellphoneback.entity.simulation.SimulationSchedule;
import com.example.cellphoneback.repository.member.MemberRepository;
import com.example.cellphoneback.repository.operation.MachineRepository;
import com.example.cellphoneback.repository.operation.OperationRepository;
import com.example.cellphoneback.repository.operation.ProductRepository;
import com.example.cellphoneback.repository.operation.TaskRepository;
import com.example.cellphoneback.repository.simulation.SimulationProductRepository;
import com.example.cellphoneback.repository.simulation.SimulationRepository;
import com.example.cellphoneback.repository.simulation.SimulationScheduleRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SimulationService {
    private final SimulationRepository simulationRepository;
    private final MachineRepository machineRepository;
    private final OperationRepository operationRepository;
    private final ProductRepository productRepository;
    private final SimulationScheduleRepository simulationScheduleRepository;
    private final SimulationProductRepository simulationProductRepository;
    private final MemberRepository memberRepository;
    private final TaskRepository taskRepository;


    @Value("${OPENAI_API_KEY}")
    private String apiKey;


    //    simulation	POST	/api/simulation	시뮬레이션 생성	admin, planner
    public CreateSimulationResponse createSimulation(Member member, CreateSimulationRequest request) {
        if (!member.getRole().equals(Role.ADMIN) && !member.getRole().equals(Role.PLANNER)) {
            throw new SecurityException("시뮬레이션 생성 권한이 없습니다.");
        }
        // 시뮬레이션 생성 로직 구현
        Simulation simulation = Simulation.builder()
                .member(member)
                .title(request.getTitle())
                .description(request.getDescription())
                .requiredStaff(request.getRequiredStaff())
                .simulationStartDate(request.getSimulationStartDate())
                .workTime(request.getWorkTime())
                .build();
        simulationRepository.save(simulation);

        List<SimulationProduct> simulationProductList =
                request.getProductList().stream().map(one -> SimulationProduct.builder()
                        .simulation(simulation)
                        .product(productRepository.findById(one).orElseThrow())
                        .build()).toList();
        simulationProductRepository.saveAll(simulationProductList);

        return CreateSimulationResponse.builder().simulation(CreateSimulationResponse.item.builder()
                .id(simulation.getId())
                .memberName(simulation.getMember().getName())
                .title(simulation.getTitle())
                .description(simulation.getDescription())
                .requiredStaff(simulation.getRequiredStaff())
                .status(simulation.getStatus())
                .simulationStartDate(simulation.getSimulationStartDate())
                .productList(simulationProductList.stream().map(sp -> sp.getProduct().getId()).toList())
                .workTime(simulation.getWorkTime()).build()).build();
    }


    //    simulation	POST	/api/simulation/{simulationId}/	시뮬레이션 실행 요청	admin, planner
    public RunSimulationResponse runSimulation(Member member, String simulationId) {

        if (!member.getRole().equals(Role.ADMIN) && !member.getRole().equals(Role.PLANNER)) {
            throw new SecurityException("시뮬레이션 실행 요청 권한이 없습니다.");
        }
        Simulation simulation = simulationRepository.findById(simulationId)
                .orElseThrow(() -> new IllegalArgumentException("해당 시뮬레이션이 존재하지 않습니다."));

        RestClient restClient = RestClient.create();

        SolveApiResult result = restClient.post()
                .uri("http://127.0.0.1:5050/api/solve")
                .body(Map.of("simulation", simulation))
                .retrieve()
                .body(SolveApiResult.class);

        simulation.setStatus(result.getStatus());
        simulation.setWorkTime(result.getMakespan());
        simulationRepository.save(simulation);

        List<SimulationSchedule> scheduleList = result.getScheduleList().stream()
                .map(one -> {
                    return SimulationSchedule.builder().simulation(simulation)
                            .task(taskRepository.findById(one.getTaskId()).orElseThrow())
                            .plannerId(memberRepository.findById(one.getPlannerId()).orElseThrow())
                            .workerId(memberRepository.findById(one.getWorkerId()).orElseThrow())
                            .startAt(simulation.getSimulationStartDate().atStartOfDay().plusHours(one.getStart()))
                            .endAt(simulation.getSimulationStartDate().atStartOfDay().plusHours(one.getEnd()))
                            .build();
                }).toList();
        simulationScheduleRepository.saveAll(scheduleList);

        return RunSimulationResponse.builder().makeSpan(result.getMakespan()).status(result.getStatus()).scheduleList(scheduleList).build();

    }

    //    simulation	DELETE	/api/simulation	시뮬레이션 삭제	admin, planner
    public DeleteSimulationResponse deleteSimulation(Member member, String simulationId) {
        if (!member.getRole().equals(Role.ADMIN) && !member.getRole().equals(Role.PLANNER)) {
            throw new SecurityException("시뮬레이션 삭제 권한이 없습니다.");
        }

        List<SimulationSchedule> simulationScheduleList = simulationScheduleRepository.findAll();
        List<SimulationSchedule> simulationSchedule = simulationScheduleList.stream()
                .filter(e -> e.getSimulation().getId().equals(simulationId)).toList();
        simulationScheduleRepository.deleteAll(simulationSchedule);

        List<SimulationProduct> simulationProductList = simulationProductRepository.findAll();
        List<SimulationProduct> deleteSimulationProduct =
                simulationProductList.stream().filter(e -> e.getSimulation().getId().equals(simulationId)).toList();
        simulationProductRepository.deleteAll(deleteSimulationProduct);

        simulationRepository.deleteById(simulationId);

        return DeleteSimulationResponse.builder().message("정상적으로 삭제 되었습니다.").build();
    }

    //    simulation	GET	/api/simulation/{simulationId}/json	시뮬레이션 단건 조회	admin, planner
    public GetSimulationResponse getSimulation(Member member, String simulationId) {
        if (!member.getRole().equals(Role.ADMIN) && !member.getRole().equals(Role.PLANNER)) {
            throw new SecurityException("시뮬레이션 단건 조회 권한이 없습니다.");
        }
        Simulation simulation = simulationRepository.findById(simulationId).orElseThrow();
        return GetSimulationResponse.builder().simulation(simulation).build();
    }

    //    simulation	GET	/api/simulation	시뮬레이션 전체 조회	admin, planner
    public GetAllSimulationResponse getAllSimulations(Member member) {
        if (!member.getRole().equals(Role.ADMIN) && !member.getRole().equals(Role.PLANNER)) {
            throw new SecurityException("시뮬레이션 전체 조회 권한이 없습니다.");
        }
        List<Simulation> simulationList = simulationRepository.findAllWithMember();

        List<GetAllSimulationResponse.Item> items = simulationList.stream()
                .map(one -> GetAllSimulationResponse.Item.builder()
                        .id(one.getId())
                        .memberName(one.getMember().getName())
                        .title(one.getTitle())
                        .description(one.getDescription())
                        .productCount(one.getSimulationProductList().size())
                        .requiredStaff(one.getRequiredStaff())
                        .status(one.getStatus())
                        .simulationStartDate(one.getSimulationStartDate())
                        .workTime(one.getWorkTime()).build()).toList();

        return GetAllSimulationResponse.builder().simulationScheduleList(items).build();
    }

    //    simulation	GET	/api/simulation/{simulationScheduleId}	작업 지시(스케쥴) 조회	admin, planner
    public GetSimulationScheduleResponse getSimulationSchedule(Member member, int simulationScheduleId) {
        if (!member.getRole().equals(Role.ADMIN) && !member.getRole().equals(Role.PLANNER)) {
            throw new SecurityException("시뮬레이션 작업 지시(스케쥴) 조회 권한이 없습니다.");
        }

        Optional<SimulationSchedule> simulationSchedule = simulationScheduleRepository.findById(simulationScheduleId);

        return GetSimulationScheduleResponse.builder().simulationSchedule(simulationSchedule).build();
    }

    // simulation POST /api/simulation/{simulationScheduleId}/summary 스케줄 ai 조언 admin, planner
    public ScheduleSummaryResponse simulationScheduleGPTAdvice(Member member, int simulationScheduleId) throws JsonProcessingException {
        if(!member.getRole().equals(Role.ADMIN) && !member.getRole().equals(Role.PLANNER)) {
            throw new SecurityException("프로그램 실행 권한이 없습니다.");
        }


        SimulationSchedule simulationSchedule = simulationScheduleRepository.findById(simulationScheduleId).orElseThrow();

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String , Object> body = new HashMap<>();

        body.put("model","gpt-4o-mini");
        body.put("instructions","");
        body.put("input",objectMapper.writeValueAsString(simulationSchedule));

        RestClient restClient = RestClient.create();
        String json = restClient
                .post()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .body(body)
                .retrieve()
                .body(String.class);
        JsonNode jsonNode = objectMapper.readTree(json);
        String msg = jsonNode.get("outPut").get(0).get("content").get(0).get("text").asText();
        String[] advice = objectMapper.readValue(msg, String[].class);

        simulationSchedule.setAiSummary(msg);
        simulationScheduleRepository.save(simulationSchedule);

        return ScheduleSummaryResponse.builder().success(true).schedule(simulationSchedule).advice(advice).build();


    }

}
