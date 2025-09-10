/*
 *  @(#)TasklistAdapter.java
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
package br.com.jtech.tasklist.adapters.output;

import br.com.jtech.tasklist.adapters.output.repositories.TasklistRepository;
import br.com.jtech.tasklist.adapters.output.repositories.entities.TasklistEntity;
import br.com.jtech.tasklist.application.core.domains.Tasklist;
import br.com.jtech.tasklist.application.ports.output.TasklistOutputGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/**
 * class TasklistAdapter
 * <p>
 * user angelo.vicente
 */
@Component
@RequiredArgsConstructor
public class TasklistAdapter implements TasklistOutputGateway {

    private final TasklistRepository repository;


    @Override
    public Tasklist create(Tasklist tasklist) {
        TasklistEntity entity = new TasklistEntity();
        entity.setName(tasklist.getName());
        entity.setDescription(tasklist.getDescription());
        entity.setColor(tasklist.getColor());

        TasklistEntity savedEntity = repository.save(entity);

        Tasklist result = new Tasklist();
        result.setId(savedEntity.getId().toString());
        result.setName(savedEntity.getName());
        result.setDescription(savedEntity.getDescription());
        result.setColor(savedEntity.getColor());
        result.setCreatedAt(savedEntity.getCreatedAt());
        result.setUpdatedAt(savedEntity.getUpdatedAt());

        return result;
    }

    @Override
    public void delete(String id) {
        repository.deleteById(UUID.fromString(id));
    }

    @Override
    public Tasklist update(Tasklist tasklist) {
        TasklistEntity entity = new TasklistEntity(
                tasklist.getId() != null ? UUID.fromString(tasklist.getId()) : null,
                tasklist.getName(),
                tasklist.getDescription(),
                tasklist.getColor(),
                tasklist.getCreatedAt(),
                tasklist.getUpdatedAt()
        );
        TasklistEntity savedEntity = repository.save(entity);
        return Tasklist.of(savedEntity);
    }

    @Override
    public List<Tasklist> findAll() {
        List<TasklistEntity> entities = repository.findAll();
        return Tasklist.of(entities);
    }

    @Override
    public Tasklist findById(String id) {
        TasklistEntity entity = repository.findById(UUID.fromString(id))
                .orElseThrow(() -> new RuntimeException("Tasklist not found with id: " + id));
        return Tasklist.of(entity);
    }

}