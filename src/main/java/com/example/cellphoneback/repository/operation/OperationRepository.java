package com.example.cellphoneback.repository.operation;

import com.example.cellphoneback.entity.operation.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationRepository extends JpaRepository<Operation, String> {
}
