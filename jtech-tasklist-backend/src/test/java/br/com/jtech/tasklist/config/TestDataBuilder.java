package br.com.jtech.tasklist.config;

import br.com.jtech.tasklist.adapters.input.protocols.TaskRequest;
import br.com.jtech.tasklist.adapters.input.protocols.TasklistRequest;
import br.com.jtech.tasklist.application.core.domains.Task;
import br.com.jtech.tasklist.application.core.domains.Tasklist;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
public class TestDataBuilder {

    public TasklistRequest buildTasklistRequest() {
        return TasklistRequest.builder()
                .id(UUID.randomUUID().toString())
                .name("Lista de Teste")
                .description("Descrição da lista de teste")
                .color("#1976D2")
                .build();
    }

    public TaskRequest buildTaskRequest() {
        return TaskRequest.builder()
                .id(UUID.randomUUID().toString())
                .tasklistId(UUID.randomUUID().toString())
                .title("Tarefa de Teste")
                .description("Descrição da tarefa de teste")
                .completed(false)
                .priority("HIGH")
                .dueDate(LocalDateTime.now().plusDays(1))
                .build();
    }

    public Tasklist buildTasklist() {
        return Tasklist.builder()
                .id(UUID.randomUUID().toString())
                .name("Lista de Teste")
                .description("Descrição da lista de teste")
                .color("#1976D2")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public Task buildTask() {
        return Task.builder()
                .id(UUID.randomUUID().toString())
                .tasklistId(UUID.randomUUID().toString())
                .title("Tarefa de Teste")
                .description("Descrição da tarefa de teste")
                .completed(false)
                .priority("HIGH")
                .dueDate(LocalDateTime.now().plusDays(1))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public List<TasklistRequest> buildMultipleTasklistRequests() {
        return Arrays.asList(
                buildTasklistRequest(),
                buildTasklistRequest(),
                buildTasklistRequest()
        );
    }

    public List<TaskRequest> buildMultipleTaskRequests() {
        return Arrays.asList(
                buildTaskRequest(),
                buildTaskRequest(),
                buildTaskRequest()
        );
    }
}
