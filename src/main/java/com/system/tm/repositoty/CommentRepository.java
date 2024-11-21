package com.system.tm.repositoty;

import com.system.tm.service.model.comment.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long>, JpaSpecificationExecutor<Comment> {

    @Query("SELECT c FROM Comment c INNER JOIN Task t ON c.task.id = :taskId")
    Page<Comment> findAllByTaskId(@Param("taskId") Long taskId, Pageable pageable);
}
