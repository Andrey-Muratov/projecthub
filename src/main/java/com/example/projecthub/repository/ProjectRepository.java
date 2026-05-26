package com.example.projecthub.repository;

import com.example.projecthub.entity.Project;
import com.example.projecthub.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    @EntityGraph(attributePaths = "owner")
    Page<Project> findAllByOwner(User owner, Pageable pageable);

    @EntityGraph(attributePaths = "owner")
    Page<Project> findAllByOwnerAndTitleContainingIgnoreCase(User owner, String title, Pageable pageable);

    @EntityGraph(attributePaths = "owner")
    Page<Project> findAllByTitleContainingIgnoreCase(String title, Pageable pageable);

    long countByOwner(User owner);

    @Override
    @EntityGraph(attributePaths = "owner")
    Page<Project> findAll(Pageable pageable);

    @Override
    @EntityGraph(attributePaths = "owner")
    Optional<Project> findById(Long id);

    @EntityGraph(attributePaths = "owner")
    @Query("""
            SELECT p FROM Project p
            WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :q, '%'))
               OR LOWER(p.description) LIKE LOWER(CONCAT('%', :q, '%'))
            ORDER BY p.id DESC
            """)
    List<Project> searchByText(String q, Pageable pageable);

    @EntityGraph(attributePaths = "owner")
    @Query("""
            SELECT p FROM Project p
            WHERE p.owner = :owner
              AND (LOWER(p.title) LIKE LOWER(CONCAT('%', :q, '%'))
                OR LOWER(p.description) LIKE LOWER(CONCAT('%', :q, '%')))
            ORDER BY p.id DESC
            """)
    List<Project> searchByTextForOwner(String q, User owner, Pageable pageable);
}
