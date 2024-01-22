package br.com.marinho.tasks.model.transport.operations.update;

import jakarta.validation.constraints.NotBlank;

public record ApplyAssigneeForm(@NotBlank String id) {
}
