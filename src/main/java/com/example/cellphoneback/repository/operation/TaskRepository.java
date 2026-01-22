package com.example.cellphoneback.repository.operation;

import com.example.cellphoneback.entity.operation.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {
}
