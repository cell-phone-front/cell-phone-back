package com.example.cellphoneback.controller.simulation;

import com.example.cellphoneback.dto.request.simulation.CreateSimulationRequest;
import com.example.cellphoneback.dto.response.simulation.*;
import com.example.cellphoneback.entity.member.Member;
import com.example.cellphoneback.service.simulation.SimulationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/simulation")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Simulation", description = "시뮬레이션 관련 API")
public class SimulationController {
    private final SimulationService simulationService;

    @Operation(summary = "시뮬레이션 생성", description = "새 시뮬레이션을 생성합니다. 관리자와 기획자만 접근할 수 있습니다.")
    @PostMapping
    public ResponseEntity<CreateSimulationResponse> createSimulation(@RequestAttribute Member member,
                                                                     @RequestBody CreateSimulationRequest request) {

        CreateSimulationResponse response = simulationService.createSimulation(member, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "시뮬레이션 실행", description = "지정된 시뮬레이션을 실행합니다. 관리자와 기획자만 접근할 수 있습니다.")
    @PostMapping("/{simulationId}")
    public ResponseEntity<RunSimulationResponse> runSimulation(@RequestAttribute Member member,
                                           @PathVariable String simulationId) throws JsonProcessingException {
        RunSimulationResponse response = simulationService.runSimulation(member, simulationId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "시뮬레이션 삭제", description = "지정된 시뮬레이션을 삭제합니다. 관리자와 기획자만 접근할 수 있습니다.")
    @DeleteMapping("/{simulationId}")
    public ResponseEntity<DeleteSimulationResponse> deleteSimulation(@RequestAttribute Member member,
                                                                     @PathVariable String simulationId) {
        DeleteSimulationResponse response = simulationService.deleteSimulation(member, simulationId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "시뮬레이션 조회", description = "지정된 시뮬레이션의 세부 정보를 조회합니다. 관리자와 기획자만 접근할 수 있습니다.")
    @GetMapping("/{simulationId}/json")
    public ResponseEntity<GetSimulationResponse> getSimulation(@RequestAttribute Member member,
                                           @PathVariable String simulationId) {
        GetSimulationResponse response = simulationService.getSimulation(member, simulationId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "모든 시뮬레이션 조회", description = "모든 시뮬레이션의 요약 정보를 조회합니다. 관리자와 기획자만 접근할 수 있습니다.")
    @GetMapping
    public ResponseEntity<GetAllSimulationResponse> getAllSimulations(@RequestAttribute Member member,
                                                                      @RequestParam(required = false) String keyword) {
        GetAllSimulationResponse response = simulationService.getAllSimulations(member, keyword);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "시뮬레이션 작업 스케줄 조회", description = "지정된 시뮬레이션의 작업 스케줄을 조회합니다. 관리자와 기획자만 접근할 수 있습니다.")
    @GetMapping({"/{simulationId}"})
    public ResponseEntity<GetSimulationScheduleResponse> getSimulationSchedule(@RequestAttribute Member member,
                                                                          @PathVariable String simulationId) {
        GetSimulationScheduleResponse response = simulationService.getSimulationSchedule(member, simulationId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "개인별 작업 스케줄 조회", description = "로그인한 사용자의 개인별 작업 스케줄을 조회합니다. 모든 사용자가 접근할 수 있습니다.")
    @GetMapping("/schedule/{memberId}")
    public ResponseEntity<SchedulePersonalResponse> getSchedulePersonal(@RequestAttribute Member member){

        SchedulePersonalResponse response = simulationService.getSchedulePersonal(member);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
