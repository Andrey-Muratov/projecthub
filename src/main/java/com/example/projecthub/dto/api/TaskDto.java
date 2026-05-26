package com.example.projecthub.dto.api;

import com.example.projecthub.entity.Task;
import com.example.projecthub.entity.TaskStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record TaskDto(
        Long id,
        String title,
        String description,
        TaskStatus status,
        LocalDate deadline,
        Long projectId,
        Long assigneeId,
        String assigneeLogin,
        LocalDateTime createdAt
) {

    public static TaskDto of(Task task) {
        return new TaskDto(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getDeadline(),
                task.getProject() != null ? task.getProject().getId() : null,
                task.getAssignee() != null ? task.getAssignee().getId() : null,
                task.getAssignee() != null ? task.getAssignee().getLogin() : null,
                task.getCreatedAt()
        );
    }
}
