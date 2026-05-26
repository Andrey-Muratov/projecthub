package com.example.projecthub.dto.api;

import com.example.projecthub.entity.Project;
import com.example.projecthub.entity.ProjectStatus;
import java.time.LocalDateTime;

public record ProjectDto(
        Long id,
        String title,
        String description,
        ProjectStatus status,
        Long ownerId,
        String ownerLogin,
        LocalDateTime createdAt
) {

    public static ProjectDto of(Project project) {
        return new ProjectDto(
                project.getId(),
                project.getTitle(),
                project.getDescription(),
                project.getStatus(),
                project.getOwner() != null ? project.getOwner().getId() : null,
                project.getOwner() != null ? project.getOwner().getLogin() : null,
                project.getCreatedAt()
        );
    }
}
