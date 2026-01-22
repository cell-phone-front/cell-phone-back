package com.example.cellphoneback.repository.simulation;

import com.example.cellphoneback.entity.simulation.SimulationProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SimulationProductRepository extends JpaRepository<SimulationProduct,Integer> {
}
