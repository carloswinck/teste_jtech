package br.com.jtech.tasklist.config.usecases;

import br.com.jtech.tasklist.adapters.output.TaskAdapter;
import br.com.jtech.tasklist.application.core.usecases.TaskUseCase;
import br.com.jtech.tasklist.application.ports.input.TaskInputGateway;
import br.com.jtech.tasklist.application.ports.output.TaskOutputGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TaskUseCaseConfig {

    @Bean
    public TaskOutputGateway taskOutputGateway(TaskAdapter taskAdapter) {
        return taskAdapter;
    }

    @Bean
    public TaskInputGateway taskInputGateway(TaskOutputGateway taskOutputGateway) {
        return new TaskUseCase(taskOutputGateway);
    }
}
