package com.example.projecthub.repository;

import com.example.projecthub.entity.Task;
import com.example.projecthub.entity.TaskAttachment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskAttachmentRepository extends JpaRepository<TaskAttachment, Long> {

    List<TaskAttachment> findByTaskOrderByUploadedAtDesc(Task task);
}
