package br.com.jtech.tasklist.adapters.output.repositories.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tasks")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tasklist_id", nullable = false)
    private UUID tasklistId;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "completed", nullable = false)
    @Builder.Default
    private boolean completed = false;

    @Column(name = "priority", length = 20)
    private String priority;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}