package br.com.jtech.tasklist.adapters.input.protocols;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequest {
    private String id;
    private String tasklistId;
    private String title;
    private String description;
    private boolean completed;
    private String priority;
    private LocalDateTime dueDate;
}