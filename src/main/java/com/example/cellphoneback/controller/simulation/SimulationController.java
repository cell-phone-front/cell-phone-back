package com.example.cellphoneback.controller.simulation;

import com.example.cellphoneback.dto.request.simulation.CreateSimulationRequest;
import com.example.cellphoneback.dto.response.simulation.CreateSimulationResponse;
import com.example.cellphoneback.dto.response.simulation.DeleteSimulationResponse;
import com.example.cellphoneback.dto.response.simulation.GetAllSimulationResponse;
import com.example.cellphoneback.dto.response.simulation.GetSimulationResponse;
import com.example.cellphoneback.entity.member.Member;
import com.example.cellphoneback.service.simulation.SimulationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@SecurityRequirement(name = "bearerAuth")
//@Tag(name = "Comment", description = "댓글 관련 API")
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/simulation")
public class SimulationController {
    private final SimulationService simulationService;

    //    simulation	POST	/api/simulation	시뮬레이션 생성	admin, planner
    @PostMapping
    public ResponseEntity<CreateSimulationResponse> createSimulation(@RequestAttribute Member member,
                                                                     @RequestBody CreateSimulationRequest request) {

        CreateSimulationResponse response = simulationService.createSimulation(member, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //    simulation	POST	/api/simulation/{simulationId}	시뮬레이션 실행 요청	admin, planner
    @PostMapping("/{simulationId}")
    public ResponseEntity<?> runSimulation(@RequestAttribute Member member,
                                           @PathVariable String simulationId) {
        simulationService.runSimulation(member, simulationId);
        return ResponseEntity.ok().build();
    }

    //    simulation	DELETE	/api/simulation	시뮬레이션 삭제	admin, planner
    @DeleteMapping("/{simulationId}")
    public ResponseEntity<DeleteSimulationResponse> deleteSimulation(@RequestAttribute Member member,
                                                                     @PathVariable String simulationId) {
        DeleteSimulationResponse response = simulationService.deleteSimulation(member, simulationId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //    simulation	GET	/api/simulation/{simulationId}/json	시뮬레이션 단건 조회	admin, planner
    @GetMapping("/{simulationId}/json")
    public ResponseEntity<?> getSimulation(@RequestAttribute Member member,
                                           @PathVariable String simulationId) {
        GetSimulationResponse response = simulationService.getSimulation(member, simulationId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //    simulation	GET	/api/simulation	시뮬레이션 전체 조회	admin, planner
    @GetMapping
    public ResponseEntity<GetAllSimulationResponse> getAllSimulations(@RequestAttribute Member member) {
        GetAllSimulationResponse response = simulationService.getAllSimulations(member);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //    simulation	GET	/api/simulation/{simulationId}	작업 지시(스케쥴) 조회	admin, planner
    @GetMapping({"/{simulationScheduleId}"})
    public ResponseEntity<?> getSimulationSchedule(@RequestAttribute Member member,
                                                                          @PathVariable int simulationScheduleId) {
        simulationService.getSimulationSchedule(member, simulationScheduleId);
        return ResponseEntity.ok().build();
    }
}
