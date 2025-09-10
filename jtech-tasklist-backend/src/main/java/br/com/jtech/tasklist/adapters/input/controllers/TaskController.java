package br.com.jtech.tasklist.adapters.input.controllers;

import br.com.jtech.tasklist.adapters.input.protocols.TaskRequest;
import br.com.jtech.tasklist.adapters.input.protocols.TaskResponse;
import br.com.jtech.tasklist.application.core.domains.Task;
import br.com.jtech.tasklist.application.ports.input.TaskInputGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskInputGateway taskInputGateway;

    @PostMapping
    public ResponseEntity<TaskResponse> create(@RequestBody TaskRequest request) {
        try {
            Task task = new Task();
            task.setId(UUID.randomUUID().toString());
            task.setTasklistId(request.getTasklistId());
            task.setTitle(request.getTitle());
            task.setDescription(request.getDescription());
            task.setCompleted(false);
            task.setPriority(request.getPriority());
            task.setDueDate(request.getDueDate());
            task.setCreatedAt(LocalDateTime.now());
            task.setUpdatedAt(LocalDateTime.now());

            Task createdTask = taskInputGateway.create(task);

            TaskResponse response = new TaskResponse();
            response.setId(createdTask.getId());
            response.setTasklistId(createdTask.getTasklistId());
            response.setTitle(createdTask.getTitle());
            response.setDescription(createdTask.getDescription());
            response.setCompleted(createdTask.isCompleted());
            response.setPriority(createdTask.getPriority());
            response.setDueDate(createdTask.getDueDate());
            response.setCreatedAt(createdTask.getCreatedAt());
            response.setUpdatedAt(createdTask.getUpdatedAt());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> findById(@PathVariable String id) {
        Task task = taskInputGateway.findById(id);
        TaskResponse response = TaskResponse.builder()
                .id(task.getId())
                .tasklistId(task.getTasklistId())
                .title(task.getTitle())
                .description(task.getDescription())
                .completed(task.isCompleted())
                .priority(task.getPriority())
                .dueDate(task.getDueDate())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> findAll() {
        List<Task> tasks = taskInputGateway.findAll();
        List<TaskResponse> responses = tasks.stream()
                .map(task -> TaskResponse.builder()
                        .id(task.getId())
                        .tasklistId(task.getTasklistId())
                        .title(task.getTitle())
                        .description(task.getDescription())
                        .completed(task.isCompleted())
                        .priority(task.getPriority())
                        .dueDate(task.getDueDate())
                        .createdAt(task.getCreatedAt())
                        .updatedAt(task.getUpdatedAt())
                        .build())
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/tasklist/{tasklistId}")
    public ResponseEntity<List<TaskResponse>> findByTasklistId(@PathVariable String tasklistId) {
        List<Task> tasks = taskInputGateway.findByTasklistId(tasklistId);
        List<TaskResponse> responses = tasks.stream()
                .map(task -> TaskResponse.builder()
                        .id(task.getId())
                        .tasklistId(task.getTasklistId())
                        .title(task.getTitle())
                        .description(task.getDescription())
                        .completed(task.isCompleted())
                        .priority(task.getPriority())
                        .dueDate(task.getDueDate())
                        .createdAt(task.getCreatedAt())
                        .updatedAt(task.getUpdatedAt())
                        .build())
                .toList();
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> update(@PathVariable String id, @RequestBody TaskRequest request) {
        Task task = Task.of(request);
        task.setId(id);
        Task updatedTask = taskInputGateway.update(task);
        TaskResponse response = TaskResponse.builder()
                .id(updatedTask.getId())
                .tasklistId(updatedTask.getTasklistId())
                .title(updatedTask.getTitle())
                .description(updatedTask.getDescription())
                .completed(updatedTask.isCompleted())
                .priority(updatedTask.getPriority())
                .dueDate(updatedTask.getDueDate())
                .createdAt(updatedTask.getCreatedAt())
                .updatedAt(updatedTask.getUpdatedAt())
                .build();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<TaskResponse> toggleCompleted(@PathVariable String id) {
        Task task = taskInputGateway.toggleCompleted(id);
        TaskResponse response = TaskResponse.builder()
                .id(task.getId())
                .tasklistId(task.getTasklistId())
                .title(task.getTitle())
                .description(task.getDescription())
                .completed(task.isCompleted())
                .priority(task.getPriority())
                .dueDate(task.getDueDate())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        taskInputGateway.delete(id);
        return ResponseEntity.noContent().build();
    }
}
