package com.system.tm.service;

import com.system.tm.service.filter.JoinType;
import com.system.tm.web.dto.comment.DeleteCommentDTO;
import com.system.tm.web.dto.comment.RequestCommentDTO;
import com.system.tm.web.dto.comment.ResponseCommentDTO;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;

public interface ICommentService {

    ResponseCommentDTO createComment(RequestCommentDTO dto);


    Page<ResponseCommentDTO> getAllCommentsByTaskId(
            Long taskId,
            String criteriaJson,
            JoinType joinType,
            Pageable pageable
    );

    ResponseCommentDTO getCommentById(Long id);

    ResponseCommentDTO editComment(RequestCommentDTO dto);

    void deleteCommentById(DeleteCommentDTO dto);
}
