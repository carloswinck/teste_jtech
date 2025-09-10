package br.com.jtech.tasklist.application.core.domains;

import br.com.jtech.tasklist.adapters.input.protocols.TasklistRequest;
import br.com.jtech.tasklist.adapters.output.repositories.entities.TasklistEntity;
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
public class Tasklist {

    private String id;
    private String name;
    private String description;
    private String color;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Tasklist fromEntity(TasklistEntity entity) {
        return new Tasklist(
                entity.getId() != null ? entity.getId().toString() : null,
                entity.getName(),
                entity.getDescription(),
                entity.getColor(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public static Tasklist fromRequest(TasklistRequest request) {
        return new Tasklist(
                request.getId() != null ? request.getId() : UUID.randomUUID().toString(),
                request.getName(),
                request.getDescription(),
                request.getColor(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    public static List<Tasklist> fromEntities(List<TasklistEntity> entities) {
        return entities.stream()
                .map(Tasklist::fromEntity)
                .collect(Collectors.toList());
    }

    public static Tasklist of(TasklistEntity entity) {
        return fromEntity(entity);
    }

    public static Tasklist of(TasklistRequest request) {
        return fromRequest(request);
    }

    public static List<Tasklist> of(List<TasklistEntity> entities) {
        return fromEntities(entities);
    }


    public TasklistEntity toEntity() {
        return TasklistEntity.builder()
                .id(id != null ? UUID.fromString(id) : null)
                .name(name)
                .description(description)
                .color(color)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }

    public void updateName(String name) {
        this.name = name;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateDescription(String description) {
        this.description = description;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateColor(String color) {
        this.color = color;
        this.updatedAt = LocalDateTime.now();
    }

}