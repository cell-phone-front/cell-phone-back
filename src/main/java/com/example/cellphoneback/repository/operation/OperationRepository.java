package com.example.cellphoneback.repository.operation;

import com.example.cellphoneback.entity.operation.OperationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationRepository extends JpaRepository<OperationEntity, String> {
}
