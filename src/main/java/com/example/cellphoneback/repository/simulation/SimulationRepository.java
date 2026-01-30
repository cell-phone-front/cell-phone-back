package com.example.cellphoneback.repository.simulation;

import com.example.cellphoneback.dto.response.simulation.GetAllSimulationResponse;
import com.example.cellphoneback.entity.simulation.Simulation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SimulationRepository extends JpaRepository<Simulation, String> {
    // memberId를 memberName으로 보내기 위한 작업
    @Query("select s from Simulation s join fetch s.member")
    List<Simulation> findAllWithMember();

}
