package com.example.cellphoneback.repository.simulation;

import com.example.cellphoneback.entity.simulation.SimulationSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SimulationScheduleRepository extends JpaRepository<SimulationSchedule,Integer> {
}
