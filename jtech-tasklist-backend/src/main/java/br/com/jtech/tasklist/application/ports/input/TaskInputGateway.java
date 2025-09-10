package br.com.jtech.tasklist.application.ports.input;

import br.com.jtech.tasklist.application.core.domains.Task;

import java.util.List;

public interface TaskInputGateway {
    Task create(Task task);

    Task findById(String id);

    List<Task> findAll();

    List<Task> findByTasklistId(String tasklistId);

    Task update(Task task);

    Task toggleCompleted(String id);

    void delete(String id);
}
