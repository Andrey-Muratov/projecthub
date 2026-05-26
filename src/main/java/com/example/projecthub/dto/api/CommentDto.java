package com.example.projecthub.dto.api;

import com.example.projecthub.entity.Comment;
import java.time.LocalDateTime;

public record CommentDto(
        Long id,
        String text,
        Long taskId,
        Long authorId,
        String authorLogin,
        LocalDateTime createdAt
) {

    public static CommentDto of(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getTask() != null ? comment.getTask().getId() : null,
                comment.getAuthor() != null ? comment.getAuthor().getId() : null,
                comment.getAuthor() != null ? comment.getAuthor().getLogin() : null,
                comment.getCreatedAt()
        );
    }
}
