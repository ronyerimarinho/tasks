package br.com.marinho.tasks.model.transport;

import br.com.marinho.tasks.model.enums.TaskStatusEnum;

import java.time.LocalDateTime;

public record TaskDTO(String title, String description, TaskStatusEnum status, LocalDateTime createdAt,
                      LocalDateTime finishedAt) {

}
