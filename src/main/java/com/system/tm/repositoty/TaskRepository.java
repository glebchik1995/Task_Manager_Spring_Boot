package com.system.tm.repositoty;

import com.system.tm.service.model.task.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {

    @Query("SELECT t FROM Task t JOIN t.comments c WHERE c.id = :commentId")
    Task findTaskByCommentId(@Param("commentId") Long commentId);
}
