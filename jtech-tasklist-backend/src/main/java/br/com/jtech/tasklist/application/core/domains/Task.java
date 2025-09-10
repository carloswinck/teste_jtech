package br.com.jtech.tasklist.application.core.domains;

import br.com.jtech.tasklist.adapters.input.protocols.TaskRequest;
import br.com.jtech.tasklist.adapters.output.repositories.entities.TaskEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    private String id;
    private String tasklistId;
    private String title;
    private String description;
    private boolean completed;
    private String priority;
    private LocalDateTime dueDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Task fromEntity(TaskEntity entity) {
        return new Task(
                entity.getId() != null ? entity.getId().toString() : null,
                entity.getTasklistId() != null ? entity.getTasklistId().toString() : null,
                entity.getTitle(),
                entity.getDescription(),
                entity.isCompleted(),
                entity.getPriority(),
                entity.getDueDate(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public static Task fromRequest(TaskRequest request) {
        LocalDateTime now = LocalDateTime.now();
        return new Task(
                request.getId() != null ? request.getId() : UUID.randomUUID().toString(),
                request.getTasklistId(),
                request.getTitle(),
                request.getDescription(),
                request.isCompleted(),
                request.getPriority(),
                request.getDueDate(),
                now, // createdAt
                now  // updatedAt
        );
    }

    public static List<Task> fromEntities(List<TaskEntity> entities) {
        return entities.stream()
                .map(Task::fromEntity)
                .collect(Collectors.toList());
    }

    public static Task of(TaskEntity entity) {
        return fromEntity(entity);
    }

    public static Task of(TaskRequest request) {
        return fromRequest(request);
    }

    public static List<Task> of(List<TaskEntity> entities) {
        return fromEntities(entities);
    }


    public TaskEntity toEntity() {
        return TaskEntity.builder()
                .id(id != null ? UUID.fromString(id) : null)
                .tasklistId(tasklistId != null ? UUID.fromString(tasklistId) : null)
                .title(title)
                .description(description)
                .completed(completed)
                .priority(priority)
                .dueDate(dueDate)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }

    public void toggleCompleted() {
        this.completed = !this.completed;
    }

}