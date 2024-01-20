package br.com.marinho.tasks.model.transport.operations.create;

import br.com.marinho.tasks.model.enums.TaskStatusEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateTaskForm(@NotBlank String title, String description, @NotNull TaskStatusEnum status) {
}
