package com.example.cellphoneback.repository.operation;

import com.example.cellphoneback.entity.operation.Machine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MachineRepository extends JpaRepository<Machine,String> {
}
