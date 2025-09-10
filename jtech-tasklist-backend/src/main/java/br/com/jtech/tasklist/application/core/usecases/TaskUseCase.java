package br.com.jtech.tasklist.application.core.usecases;

import br.com.jtech.tasklist.application.core.domains.Task;
import br.com.jtech.tasklist.application.ports.input.TaskInputGateway;
import br.com.jtech.tasklist.application.ports.output.TaskOutputGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskUseCase implements TaskInputGateway {

    private final TaskOutputGateway taskOutputGateway;

    @Override
    public Task create(Task task) {
        return taskOutputGateway.create(task);
    }

    @Override
    public Task findById(String id) {
        return taskOutputGateway.findById(id);
    }

    @Override
    public List<Task> findAll() {
        return taskOutputGateway.findAll();
    }

    @Override
    public List<Task> findByTasklistId(String tasklistId) {
        return taskOutputGateway.findByTasklistId(tasklistId);
    }

    @Override
    public Task update(Task task) {
        return taskOutputGateway.update(task);
    }

    @Override
    public Task toggleCompleted(String id) {
        Task task = taskOutputGateway.findById(id);
        task.toggleCompleted();
        return taskOutputGateway.update(task);
    }

    @Override
    public void delete(String id) {
        taskOutputGateway.delete(id);
    }
}
