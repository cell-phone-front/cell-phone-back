
package com.example.cellphoneback.service.simulation;

import com.example.cellphoneback.dto.request.simulation.CreateSimulationRequest;
import com.example.cellphoneback.dto.response.simulation.*;
import com.example.cellphoneback.entity.member.Member;
import com.example.cellphoneback.entity.member.Role;
import com.example.cellphoneback.entity.operation.Task;
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
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
    public RunSimulationResponse runSimulation(Member member, String simulationId) throws JsonProcessingException {

        if (!member.getRole().equals(Role.ADMIN) && !member.getRole().equals(Role.PLANNER)) {
            throw new SecurityException("시뮬레이션 실행 요청 권한이 없습니다.");
        }
        Simulation simulation = simulationRepository.findById(simulationId)
                .orElseThrow(() -> new IllegalArgumentException("해당 시뮬레이션이 존재하지 않습니다."));

        RestClient python = RestClient.create();

        SolveApiResult result = python.post()
                .uri("http://127.0.0.1:5050/api/solve")
                .body(Map.of("simulation", simulation))
                .retrieve()
                .body(SolveApiResult.class);

        simulation.setStatus(result.getStatus());
        simulation.setWorkTime(result.getMakespan());
        simulationRepository.save(simulation);

        // 팀장 공정별 1명 고정 배치
        List<Member> planners = memberRepository.findByRole(Role.PLANNER);
        Map<String, Member> plannerByTask = new HashMap<>();

        Iterator<Member> plannerIterator = planners.iterator();

        // 직원 그룹별 리스트 생성
        List<Member> workers = memberRepository.findByRole(Role.WORKER);
        Map<String, List<Member>> workerByTeam = workers.stream()
                .collect(Collectors.groupingBy(m -> m.getWorkTeam()));

        // 직원별 다음 배정 가능 시간
        Map<String, LocalDateTime> workerAvailableAt = new HashMap<>();

        List<SimulationSchedule> scheduleList = new ArrayList<>();

        for (SolveApiResult.TaskSchedule one : result.getScheduleList()) {

            Task task = taskRepository.findById(one.getTaskId()).orElseThrow();

            // 팀방 배정 공정별 1명 고정
            Member planner = plannerByTask.computeIfAbsent(
                    task.getId(),
                    k -> {
                        if (!plannerIterator.hasNext()) {
                            throw new NoSuchElementException("팀장 수가 부족합니다.");
                        }
                        return plannerIterator.next();
                    });
            // 작업 시간 계산
            LocalDateTime startAt = simulation.getSimulationStartDate().atStartOfDay().plusMinutes(one.getStart());


            LocalDateTime endAt = simulation.getSimulationStartDate()
                    .atStartOfDay().plusMinutes(one.getEnd());

            // 공정별 필요한 직원 수

            Member worker = workers.stream()
                    .filter(w -> {
                        LocalDateTime availableAt = workerAvailableAt.get(w.getId());
                        return availableAt == null || !availableAt.isAfter(startAt);
                    })
                    .min(Comparator.comparing(w -> workerAvailableAt.getOrDefault(w.getId(), LocalDateTime.MIN)))
                    .orElseThrow(() -> new IllegalStateException("해당 시간에 투입 가능한 직원이 없습니다."));

            workerAvailableAt.put(worker.getId(), endAt);

            scheduleList.add(
                    SimulationSchedule.builder()
                            .simulation(simulation)
                            .product(productRepository.findById(one.getProductId()).orElseThrow())
                            .task(task)
                            .plannerId(planner)
                            .workerId(worker)
                            .startAt(startAt)
                            .endAt(endAt)
                            .build()
            );
        }
        simulationScheduleRepository.saveAll(scheduleList);

        // 여기서부터는 OpenAI API 에 대한 로직
        List<SimulationSchedule> schedules = simulationScheduleRepository.findAll().stream()
                .filter(s -> s.getSimulation().getId().equals(simulationId)).toList();

        if (scheduleList.isEmpty()){
            throw new NoSuchElementException("존재하지 않는 스케줄 입니다.");
        }

        // JPA Entity(SimulationSchedule)를
        // API 응답용 DTO(GetSimulationScheduleResponse)로 변환하는 로직
        // Entity를 재사용하기 위해 필요한 로직
        List<GetSimulationScheduleResponse.Item> items = schedules.stream()
                .map(schedule -> GetSimulationScheduleResponse.Item.fromEntity(schedule)).toList();

        // Java 객체, LocalDateTime 을 JSON으로 받기위해
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // OpenAI API 보낼 요청 JSON 전체 구조를 만들기 위한 Map
        Map<String, Object> body = new HashMap<>();

        // 사용할 모델 지정, 지시문 역할,
        // objectMapper로 JSON 문자열로 변환 input에 문자열(text) 형태로 전달
        body.put("model", "gpt-4o-mini");
        body.put("instructions", "내가 보내는 작업 스케쥴에 대해 적절한 개선 포인트에 대한 피드백을 보내줘.");
        body.put("input", objectMapper.writeValueAsString(items));

        // RestClient로 OpenAI 호출
        RestClient openAI = RestClient.create("https://api.openai.com/v1/responses");
        String json = openAI
                .post()
                .header("Content-type", "application/json") // JSON 형식으로 요청 보낸다는 의미
                .header("Authorization", "Bearer " + apiKey)
                .body(body) // Map을 JSON으로 변환해서 전송
                .retrieve()
                .body(String.class); // 응답을 String(json 원문) 으로 받음

        // GPT 응답 파싱 (path 사용으로 NPE 방지)
        System.out.println(json);
        JsonNode root = objectMapper.readTree(json); // json 문자열 트리 구조로 변환
        JsonNode output = root.path("output");

        String msg = output.path(0) // 첫번째 응답 르록
                .path("content").path(0) // 실제 텍스트가 들어있는 객체
                .path("text") // GPT가 생성한 자연어 답변
                .asText(); // String으로 변환
        System.out.println(msg);


        simulation.setAiSummary(msg);
        simulationRepository.save(simulation);

        return RunSimulationResponse.builder().status(result.getStatus()).aiSummary(msg).build();

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

    //    simulation	GET	/api/simulation/{simulationId}/json	시뮬레이션 세팅 데이터 조회	admin, planner
    public GetSimulationResponse getSimulation(Member member, String simulationId) {
        if (!member.getRole().equals(Role.ADMIN) && !member.getRole().equals(Role.PLANNER)) {
            throw new SecurityException("시뮬레이션 단건 조회 권한이 없습니다.");
        }
        Simulation simulation = simulationRepository.findById(simulationId).orElseThrow();
        return GetSimulationResponse.builder().simulation(simulation).build();
    }

    //    simulation	GET	/api/simulation	시뮬레이션 전체 조회	admin, planner
    public GetAllSimulationResponse getAllSimulations(Member member, String keyword) {
        if (!member.getRole().equals(Role.ADMIN) && !member.getRole().equals(Role.PLANNER)) {
            throw new SecurityException("시뮬레이션 전체 조회 권한이 없습니다.");
        }

        List<GetAllSimulationResponse.Item> simulationList = simulationRepository.findAllWithMember().stream()
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
        List<GetAllSimulationResponse.Item> simulations = simulationList.stream()
                .filter(s -> {
                    if(keyword == null || keyword.isBlank())
                        return true;

                    String k = keyword.trim().toLowerCase().replaceAll("\\s+", "");

                    return s.getMemberName() != null && s.getMemberName().toLowerCase().replaceAll("\\s+", "").contains(k)
                            || s.getTitle() != null && s.getTitle().toLowerCase().replaceAll("\\s+", "").contains(k)
                            || s.getDescription() != null && s.getDescription().toLowerCase().replaceAll("\\s+", "").contains(k);
                }).toList();

        return GetAllSimulationResponse.builder().simulationScheduleList(simulations).build();
    }

    //    simulation	GET	/api/simulation/{simulationId}	작업 지시(스케쥴) 조회	admin, planner
    public GetSimulationScheduleResponse getSimulationSchedule(Member member, String simulationId) {
        if (!member.getRole().equals(Role.ADMIN) && !member.getRole().equals(Role.PLANNER)) {
            throw new SecurityException("시뮬레이션 작업 지시(스케쥴) 조회 권한이 없습니다.");
        }
        Simulation simulation =  simulationRepository.findById(simulationId).orElseThrow();
        List<SimulationSchedule> scheduleList = simulationScheduleRepository.findAll();
        List<SimulationSchedule> selectSchedule = scheduleList.stream()
                .filter(e -> e.getSimulation().getId().equals(simulationId)).toList();
        List<GetSimulationScheduleResponse.Item> items =
                selectSchedule.stream().map(entity -> GetSimulationScheduleResponse.Item.fromEntity(entity)).toList();

        String aiSummary = simulation.getAiSummary();

        return GetSimulationScheduleResponse.builder().scheduleList(items).aiSummary(aiSummary).build();
    }

    // simulation GET /api/simulation/schedule/{memberId} 작업스케줄 개인 조회 all
    public SchedulePersonalResponse getSchedulePersonal (Member member){
        Member user = memberRepository.findById(member.getId())
                .orElseThrow(() -> new NoSuchElementException("로그인 사용자 정보가 없습니다."));

        List<SimulationSchedule> scheduleList = simulationScheduleRepository.findAll().stream()
                .filter(s -> s.getWorkerId().getId().equals(user.getId()) ||
                        s.getPlannerId().getId().equals(user.getId())).toList();
        List<SchedulePersonalResponse.Item> items =
                scheduleList.stream().map(entity -> SchedulePersonalResponse.Item.fromEntity(entity)).toList();

        return SchedulePersonalResponse.builder().schedule(items).build();
    }

}
