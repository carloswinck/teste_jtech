package br.com.jtech.tasklist.adapters.output.repositories;

import br.com.jtech.tasklist.adapters.output.repositories.entities.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, UUID> {

    List<TaskEntity> findByTasklistId(UUID tasklistId);

    List<TaskEntity> findByTasklistIdAndCompleted(UUID tasklistId, boolean completed);

    @Query("SELECT t FROM TaskEntity t WHERE t.tasklistId = :tasklistId ORDER BY t.createdAt DESC")
    List<TaskEntity> findByTasklistIdOrderByCreatedAtDesc(@Param("tasklistId") UUID tasklistId);

    @Query("SELECT t FROM TaskEntity t WHERE t.tasklistId = :tasklistId ORDER BY t.priority DESC, t.createdAt DESC")
    List<TaskEntity> findByTasklistIdOrderByPriorityAndCreatedAtDesc(@Param("tasklistId") UUID tasklistId);

    long countByTasklistId(UUID tasklistId);

    long countByTasklistIdAndCompleted(UUID tasklistId, boolean completed);
}
