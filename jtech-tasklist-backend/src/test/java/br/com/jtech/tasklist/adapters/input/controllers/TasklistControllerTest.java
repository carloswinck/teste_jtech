package br.com.jtech.tasklist.adapters.input.controllers;

import br.com.jtech.tasklist.adapters.input.protocols.TasklistRequest;
import br.com.jtech.tasklist.application.core.domains.Tasklist;
import br.com.jtech.tasklist.application.ports.input.TasklistInputGateway;
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

@WebMvcTest(TasklistController.class)
@Import(TestSecurityConfig.class)
class TasklistControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TasklistInputGateway tasklistInputGateway;

    @Test
    void shouldCreateTasklist() throws Exception {
        // Given
        TasklistRequest request = TasklistRequest.builder()
                .name("Test Tasklist")
                .description("Test Description")
                .color("#1976D2")
                .build();

        Tasklist tasklist = Tasklist.builder()
                .id(UUID.randomUUID().toString())
                .name("Test Tasklist")
                .description("Test Description")
                .color("#1976D2")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(tasklistInputGateway.create(any(Tasklist.class))).thenReturn(tasklist);

        // When & Then
        mockMvc.perform(post("/api/v1/tasklists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Test Tasklist\",\"description\":\"Test Description\",\"color\":\"#1976D2\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test Tasklist"))
                .andExpect(jsonPath("$.description").value("Test Description"))
                .andExpect(jsonPath("$.color").value("#1976D2"));
    }

    @Test
    void shouldGetAllTasklists() throws Exception {
        // Given
        Tasklist tasklist = Tasklist.builder()
                .id(UUID.randomUUID().toString())
                .name("Test Tasklist")
                .description("Test Description")
                .color("#1976D2")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(tasklistInputGateway.findAll()).thenReturn(List.of(tasklist));

        // When & Then
        mockMvc.perform(get("/api/v1/tasklists"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Tasklist"))
                .andExpect(jsonPath("$[0].description").value("Test Description"))
                .andExpect(jsonPath("$[0].color").value("#1976D2"));
    }
}
