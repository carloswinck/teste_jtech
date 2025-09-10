package br.com.jtech.tasklist.adapters.input.controllers;

import br.com.jtech.tasklist.adapters.input.protocols.TaskRequest;
import br.com.jtech.tasklist.application.core.domains.Task;
import br.com.jtech.tasklist.application.ports.input.TaskInputGateway;
import br.com.jtech.tasklist.config.TestSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
@Import(TestSecurityConfig.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskInputGateway taskInputGateway;

    @Test
    void shouldCreateTask() throws Exception {
        // Given
        TaskRequest request = TaskRequest.builder()
                .title("Test Task")
                .description("Test Description")
                .completed(false)
                .priority("HIGH")
                .build();

        Task task = Task.builder()
                .id(UUID.randomUUID().toString())
                .title("Test Task")
                .description("Test Description")
                .completed(false)
                .priority("HIGH")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(taskInputGateway.create(any(Task.class))).thenReturn(task);

        // When & Then
        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Test Task\",\"description\":\"Test Description\",\"completed\":false,\"priority\":\"HIGH\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test Task"))
                .andExpect(jsonPath("$.description").value("Test Description"))
                .andExpect(jsonPath("$.completed").value(false))
                .andExpect(jsonPath("$.priority").value("HIGH"));
    }

    @Test
    void shouldGetAllTasks() throws Exception {
        // Given
        Task task = Task.builder()
                .id(UUID.randomUUID().toString())
                .title("Test Task")
                .description("Test Description")
                .completed(false)
                .priority("HIGH")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(taskInputGateway.findAll()).thenReturn(List.of(task));

        // When & Then
        mockMvc.perform(get("/api/v1/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Task"))
                .andExpect(jsonPath("$[0].description").value("Test Description"))
                .andExpect(jsonPath("$[0].completed").value(false))
                .andExpect(jsonPath("$[0].priority").value("HIGH"));
    }
}
