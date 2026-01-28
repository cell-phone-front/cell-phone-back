package com.example.cellphoneback.controller.simulation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@SecurityRequirement(name = "bearerAuth")
//@Tag(name = "Comment", description = "댓글 관련 API")
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/simulation")
public class SimulationController {

//    simulation	POST	/api/simulation	시뮬레이션 생성	admin, planner
    @PostMapping
    public ResponseEntity<?> createSimulation() {
        return ResponseEntity.ok().build();
    }

//    simulation	POST	/api/simulation/{simulationId}	시뮬레이션 실행 요청	admin, planner
    @PostMapping("/{simulationId}")
    public ResponseEntity<?> editSimulationRun() {
        return ResponseEntity.ok().build();
    }

//    simulation	DELETE	/api/simulation	시뮬레이션 삭제	admin, planner
    @DeleteMapping
    public ResponseEntity<?> deleteSimulationRun() {
        return ResponseEntity.ok().build();
    }

//    simulation	GET	/api/simulation/{simulationId}/json	시뮬레이션 단건 조회	admin, planner
    @GetMapping("/{simulationId}/json")
    public ResponseEntity<?> getSimulation() {
        return ResponseEntity.ok().build();
    }

//    simulation	GET	/api/simulation	시뮬레이션 전체 조회	admin, planner
    @GetMapping
    public ResponseEntity<?> getAllSimulations() {
        return ResponseEntity.ok().build();
    }
//    simulation	GET	/api/simulation/{simulationId}	작업 지시(스케쥴) 조회	admin, planner
    @GetMapping({"/{simulationId}"})
    public ResponseEntity<?> getSimulationSchedule() {
        return ResponseEntity.ok().build();
    }
}
