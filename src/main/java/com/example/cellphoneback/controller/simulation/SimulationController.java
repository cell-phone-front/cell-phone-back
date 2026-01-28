package com.example.cellphoneback.controller.simulation;

import com.example.cellphoneback.dto.response.simulation.CreateSimulationResponse;
import com.example.cellphoneback.entity.member.Member;
import com.example.cellphoneback.entity.simulation.Simulation;
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
    public ResponseEntity<CreateSimulationResponse> createSimulation(@RequestAttribute Member member) {

        simulationService.createSimulation(member);

        return ResponseEntity
                .ok().build();
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
    public ResponseEntity<?> deleteSimulation(@RequestAttribute Member member,
                                              @PathVariable String simulationId) {
        simulationService.deleteSimulation(member, simulationId);
        return ResponseEntity.ok().build();
    }

    //    simulation	GET	/api/simulation/{simulationId}/json	시뮬레이션 단건 조회	admin, planner
    @GetMapping("/{simulationId}/json")
    public ResponseEntity<?> getSimulation(@RequestAttribute Member member,
                                           @PathVariable String simulationId) {
        simulationService.getSimulation(member, simulationId);
        return ResponseEntity.ok().build();
    }

    //    simulation	GET	/api/simulation	시뮬레이션 전체 조회	admin, planner
    @GetMapping
    public ResponseEntity<?> getAllSimulations(@RequestAttribute Member member) {
        simulationService.getAllSimulations(member);
        return ResponseEntity.ok().build();
    }

    //    simulation	GET	/api/simulation/{simulationId}	작업 지시(스케쥴) 조회	admin, planner
    @GetMapping({"/{simulationId}"})
    public ResponseEntity<?> getSimulationSchedule(@RequestAttribute Member member,
                                                   @PathVariable String simulationId) {
        simulationService.getSimulationSchedule(member, simulationId);
        return ResponseEntity.ok().build();
    }
}
