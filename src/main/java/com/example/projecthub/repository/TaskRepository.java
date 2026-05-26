package com.example.projecthub.repository;

import com.example.projecthub.entity.Project;
import com.example.projecthub.entity.Task;
import com.example.projecthub.entity.TaskStatus;
import com.example.projecthub.entity.User;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.history.RevisionRepository;

public interface TaskRepository extends JpaRepository<Task, Long>, RevisionRepository<Task, Long, Integer> {

    @EntityGraph(attributePaths = {"assignee", "tags"})
    Page<Task> findAllByProject(Project project, Pageable pageable);

    @EntityGraph(attributePaths = {"assignee", "tags"})
    Page<Task> findAllByProjectAndStatus(Project project, TaskStatus status, Pageable pageable);

    @EntityGraph(attributePaths = {"assignee", "tags"})
    List<Task> findAllByProject(Project project);

    long countByProject(Project project);

    long countByStatus(TaskStatus status);

    @Override
    @EntityGraph(attributePaths = {"assignee", "project", "project.owner", "tags"})
    Optional<Task> findById(Long id);

    @Query("""
            SELECT t.project.id, t.status, COUNT(t)
            FROM Task t
            WHERE t.project.id IN :projectIds
            GROUP BY t.project.id, t.status
            """)
    List<Object[]> countByProjectIdGroupByStatus(Collection<Long> projectIds);

    @EntityGraph(attributePaths = {"project", "assignee"})
    List<Task> findTop10ByAssigneeAndStatusOrderByDeadlineAsc(User assignee, TaskStatus status);

    @EntityGraph(attributePaths = {"project", "assignee"})
    List<Task> findTop10ByAssigneeAndDeadlineAndStatusNotInOrderByDeadlineAsc(
            User assignee, LocalDate deadline, Collection<TaskStatus> excluded);

    @EntityGraph(attributePaths = {"project", "assignee"})
    List<Task> findTop10ByAssigneeAndDeadlineBeforeAndStatusNotInOrderByDeadlineAsc(
            User assignee, LocalDate deadline, Collection<TaskStatus> excluded);

    long countByAssigneeAndStatus(User assignee, TaskStatus status);

    long countByAssigneeAndDeadlineAndStatusNotIn(
            User assignee, LocalDate deadline, Collection<TaskStatus> excluded);

    long countByAssigneeAndDeadlineBeforeAndStatusNotIn(
            User assignee, LocalDate deadline, Collection<TaskStatus> excluded);

    @EntityGraph(attributePaths = {"project", "assignee"})
    @Query("""
            SELECT t FROM Task t
            WHERE LOWER(t.title) LIKE LOWER(CONCAT('%', :q, '%'))
               OR LOWER(t.description) LIKE LOWER(CONCAT('%', :q, '%'))
            ORDER BY t.id DESC
            """)
    List<Task> searchByText(String q, Pageable pageable);

    @EntityGraph(attributePaths = {"project", "assignee"})
    @Query("""
            SELECT t FROM Task t
            WHERE t.project.owner = :owner
              AND (LOWER(t.title) LIKE LOWER(CONCAT('%', :q, '%'))
                OR LOWER(t.description) LIKE LOWER(CONCAT('%', :q, '%')))
            ORDER BY t.id DESC
            """)
    List<Task> searchByTextForOwner(String q, User owner, Pageable pageable);

    @Query("""
            SELECT t.status, COUNT(t) FROM Task t
            WHERE t.assignee = :assignee
            GROUP BY t.status
            """)
    List<Object[]> countByAssigneeGroupByStatus(User assignee);

    @Query("""
            SELECT CAST(t.updatedAt AS date) AS day, COUNT(t)
            FROM Task t
            WHERE t.assignee = :assignee
              AND t.status = com.example.projecthub.entity.TaskStatus.DONE
              AND t.updatedAt >= :from
            GROUP BY CAST(t.updatedAt AS date)
            ORDER BY day ASC
            """)
    List<Object[]> countDoneByAssigneeSince(User assignee, LocalDateTime from);

    @Query("""
            SELECT t FROM Task t
            WHERE t.project = :project
              AND t.deadline BETWEEN :from AND :to
            ORDER BY t.deadline ASC
            """)
    List<Task> findByProjectAndDeadlineBetween(Project project, LocalDate from, LocalDate to);
}
