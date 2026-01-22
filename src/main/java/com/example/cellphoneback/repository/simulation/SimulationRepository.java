package com.example.cellphoneback.repository.simulation;

import com.example.cellphoneback.entity.simulation.SimulationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SimulationRepository extends JpaRepository<SimulationEntity, String> {
}
