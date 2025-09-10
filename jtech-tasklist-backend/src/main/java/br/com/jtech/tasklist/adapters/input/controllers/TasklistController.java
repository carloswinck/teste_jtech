/*
 *  @(#)TasklistController.java
 *
 *  Copyright (c) J-Tech Solucoes em Informatica.
 *  All Rights Reserved.
 *
 *  This software is the confidential and proprietary information of J-Tech.
 *  ("Confidential Information"). You shall not disclose such Confidential
 *  Information and shall use it only in accordance with the terms of the
 *  license agreement you entered into with J-Tech.
 *
 */
package br.com.jtech.tasklist.adapters.input.controllers;

import br.com.jtech.tasklist.adapters.input.protocols.TasklistRequest;
import br.com.jtech.tasklist.adapters.input.protocols.TasklistResponse;
import br.com.jtech.tasklist.application.core.domains.Tasklist;
import br.com.jtech.tasklist.application.ports.input.TasklistInputGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasklists")
@RequiredArgsConstructor
public class TasklistController {

    private final TasklistInputGateway tasklistInputGateway;

    @PostMapping
    public ResponseEntity<TasklistResponse> create(@RequestBody TasklistRequest request) {
        Tasklist tasklist = Tasklist.of(request);
        Tasklist createdTasklist = tasklistInputGateway.create(tasklist);

        TasklistResponse response = TasklistResponse.builder()
                .id(createdTasklist.getId())
                .name(createdTasklist.getName())
                .description(createdTasklist.getDescription())
                .color(createdTasklist.getColor())
                .createdAt(createdTasklist.getCreatedAt())
                .updatedAt(createdTasklist.getUpdatedAt())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TasklistResponse> findById(@PathVariable String id) {
        Tasklist tasklist = tasklistInputGateway.findById(id);
        TasklistResponse response = TasklistResponse.builder()
                .id(tasklist.getId())
                .name(tasklist.getName())
                .description(tasklist.getDescription())
                .color(tasklist.getColor())
                .createdAt(tasklist.getCreatedAt())
                .updatedAt(tasklist.getUpdatedAt())
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<TasklistResponse>> findAll() {
        List<Tasklist> tasklists = tasklistInputGateway.findAll();
        List<TasklistResponse> responses = tasklists.stream()
                .map(tasklist -> TasklistResponse.builder()
                        .id(tasklist.getId())
                        .name(tasklist.getName())
                        .description(tasklist.getDescription())
                        .color(tasklist.getColor())
                        .createdAt(tasklist.getCreatedAt())
                        .updatedAt(tasklist.getUpdatedAt())
                        .build())
                .toList();
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TasklistResponse> update(@PathVariable String id, @RequestBody TasklistRequest request) {
        Tasklist tasklist = Tasklist.of(request);
        tasklist.setId(id);
        Tasklist updatedTasklist = tasklistInputGateway.update(tasklist);
        TasklistResponse response = TasklistResponse.builder()
                .id(updatedTasklist.getId())
                .name(updatedTasklist.getName())
                .description(updatedTasklist.getDescription())
                .color(updatedTasklist.getColor())
                .createdAt(updatedTasklist.getCreatedAt())
                .updatedAt(updatedTasklist.getUpdatedAt())
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        tasklistInputGateway.delete(id);
        return ResponseEntity.noContent().build();
    }
}