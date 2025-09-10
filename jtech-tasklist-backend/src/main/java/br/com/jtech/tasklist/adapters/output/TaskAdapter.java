package br.com.jtech.tasklist.adapters.output;

import br.com.jtech.tasklist.adapters.output.repositories.TaskRepository;
import br.com.jtech.tasklist.adapters.output.repositories.entities.TaskEntity;
import br.com.jtech.tasklist.application.core.domains.Task;
import br.com.jtech.tasklist.application.ports.output.TaskOutputGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TaskAdapter implements TaskOutputGateway {

    private final TaskRepository taskRepository;

    @Override
    public Task create(Task task) {
        TaskEntity entity = new TaskEntity(
                null,
                task.getTasklistId() != null ? UUID.fromString(task.getTasklistId()) : null,
                task.getTitle(),
                task.getDescription(),
                task.isCompleted(),
                task.getPriority(),
                task.getDueDate(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );

        TaskEntity savedEntity = taskRepository.save(entity);
        return Task.of(savedEntity);
    }

    @Override
    public Task findById(String id) {
        TaskEntity entity = taskRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
        return Task.of(entity);
    }

    @Override
    public List<Task> findAll() {
        List<TaskEntity> entities = taskRepository.findAll();
        return Task.of(entities);
    }

    @Override
    public List<Task> findByTasklistId(String tasklistId) {
        List<TaskEntity> entities = taskRepository.findByTasklistId(UUID.fromString(tasklistId));
        return Task.of(entities);
    }

    @Override
    public Task update(Task task) {
        TaskEntity entity = task.toEntity();
        TaskEntity savedEntity = taskRepository.save(entity);
        return Task.of(savedEntity);
    }

    @Override
    public void delete(String id) {
        taskRepository.deleteById(UUID.fromString(id));
    }
}
