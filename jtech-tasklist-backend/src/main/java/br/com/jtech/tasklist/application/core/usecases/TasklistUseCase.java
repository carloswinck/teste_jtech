/*
 *  @(#)TasklistUseCase.java
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
package br.com.jtech.tasklist.application.core.usecases;

import br.com.jtech.tasklist.application.core.domains.Tasklist;
import br.com.jtech.tasklist.application.ports.input.TasklistInputGateway;
import br.com.jtech.tasklist.application.ports.output.TasklistOutputGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TasklistUseCase implements TasklistInputGateway {

    private final TasklistOutputGateway tasklistOutputGateway;

    @Override
    public Tasklist create(Tasklist tasklist) {
        return tasklistOutputGateway.create(tasklist);
    }

    @Override
    public Tasklist findById(String id) {
        return tasklistOutputGateway.findById(id);
    }

    @Override
    public List<Tasklist> findAll() {
        return tasklistOutputGateway.findAll();
    }

    @Override
    public Tasklist update(Tasklist tasklist) {
        return tasklistOutputGateway.update(tasklist);
    }

    @Override
    public void delete(String id) {
        tasklistOutputGateway.delete(id);
    }
}