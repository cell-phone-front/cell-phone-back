
package com.example.cellphoneback.service.simulation;

import com.example.cellphoneback.entity.member.Member;
import com.example.cellphoneback.entity.member.Role;
import com.example.cellphoneback.repository.operation.MachineRepository;
import com.example.cellphoneback.repository.operation.OperationRepository;
import com.example.cellphoneback.repository.operation.ProductRepository;
import com.example.cellphoneback.repository.simulation.SimulationRepository;
import com.example.cellphoneback.repository.simulation.SimulationScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SimulationService {
    private final SimulationRepository simulationRepository;
    private final MachineRepository machineRepository;
    private final OperationRepository operationRepository;
    private final ProductRepository productRepository;
    private final SimulationScheduleRepository simulationScheduleRepository;

    //    simulation	POST	/api/simulation	시뮬레이션 생성	admin, planner
    public void createSimulation(Member member) {
        if (!member.getRole().equals(Role.ADMIN) && !member.getRole().equals(Role.PLANNER)) {
            throw new SecurityException("시뮬레이션 생성 권한이 없습니다.");
        }


    }


    //    simulation	POST	/api/{simulationId}/simulation	시뮬레이션 실행 요청	admin, planner
    public void runSimulation(Member member, String simulationId) {
        if (!member.getRole().equals(Role.ADMIN) && !member.getRole().equals(Role.PLANNER)) {
            throw new SecurityException("시뮬레이션 실행 요청 권한이 없습니다.");
        }
        // 시뮬레이션 실행 로직 구현
    }

    //    simulation	DELETE	/api/simulation	시뮬레이션 삭제	admin, planner
    public void deleteSimulation(Member member, String simulationId) {
        if (!member.getRole().equals(Role.ADMIN) && !member.getRole().equals(Role.PLANNER)) {
            throw new SecurityException("시뮬레이션 삭제 권한이 없습니다.");
        }
        // 시뮬레이션 삭제 로직 구현
    }

    //    simulation	GET	/api/simulation/{simulationId}/json	시뮬레이션 단건 조회	admin, planner
    public void getSimulation(Member member, String simulationId) {
        if (!member.getRole().equals(Role.ADMIN) && !member.getRole().equals(Role.PLANNER)) {
            throw new SecurityException("시뮬레이션 단건 조회 권한이 없습니다.");
        }
        // 시뮬레이션 단건 조회 로직 구현
    }

    //    simulation	GET	/api/simulation	시뮬레이션 전체 조회	admin, planner
    public void getAllSimulations(Member member) {
        if (!member.getRole().equals(Role.ADMIN) && !member.getRole().equals(Role.PLANNER)) {
            throw new SecurityException("시뮬레이션 전체 조회 권한이 없습니다.");
        }
        // 시뮬레이션 전체 조회 로직 구현
    }

    //    simulation	GET	/api/simulation/{simulationId}	작업 지시(스케쥴) 조회	admin, planner
    public void getSimulationSchedule(Member member, String simulationId) {
        if (!member.getRole().equals(Role.ADMIN) && !member.getRole().equals(Role.PLANNER)) {
            throw new SecurityException("시뮬레이션 작업 지시(스케쥴) 조회 권한이 없습니다.");
        }
        // 작업 지시(스케쥴) 조회 로직 구현
    }

}
