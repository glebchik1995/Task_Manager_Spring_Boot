package com.system.tm.web.controller.common;

import com.system.tm.aspect.log.LogInfo;
import com.system.tm.service.ICommentService;
import com.system.tm.service.filter.JoinType;
import com.system.tm.validator.marker.OnCreate;
import com.system.tm.validator.marker.OnUpdate;
import com.system.tm.web.dto.comment.DeleteCommentDTO;
import com.system.tm.web.dto.comment.RequestCommentDTO;
import com.system.tm.web.dto.comment.ResponseCommentDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "COMMENT Controller",
        description = "CRUD OPERATIONS WITH COMMENTS"
)
@RestController
@RequestMapping(value = "/api/v1/comment")
@RequiredArgsConstructor
@LogInfo
public class CommentController {

    private final ICommentService commentService;

    @PostMapping
    @Operation(summary = "Написать коммент к задаче")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("@cse.canAccessUserTask(#dto.taskId)")
    public ResponseCommentDTO createComment(@Validated(OnCreate.class) @RequestBody RequestCommentDTO dto) {
        return commentService.createComment(dto);
    }

    @GetMapping("/task/{taskId}")
    @Operation(summary = "Получить все комментарии по ID задачи")
    @PreAuthorize("@cse.canAccessUserTask(#taskId)")
    public Page<ResponseCommentDTO> getAllCommentsByTaskId(
            @PathVariable @Min(1) final Long taskId,
            @RequestParam(required = false) String criteriaJson,
            @RequestParam(required = false) JoinType joinType,
            @ParameterObject Pageable pageable
    ) {
        return commentService.getAllCommentsByTaskId(
                taskId,
                criteriaJson,
                joinType,
                pageable
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить комментарий по ID")
    @PreAuthorize("@cse.canAccessUserComment(#id)")
    public ResponseCommentDTO getComment(@PathVariable @Min(1) Long id) {
        return commentService.getCommentById(id);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Редактировать комментарий")
    @PreAuthorize("@cse.canAccessUserTask(#dto.taskId)")
    public ResponseCommentDTO editComment(@Validated(OnUpdate.class) @RequestBody RequestCommentDTO dto) {
        return commentService.editComment(dto);
    }

    @DeleteMapping("/{taskId}/{commentId}")
    @Operation(summary = "Удалить комментарий по ID")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("@cse.canAccessUserTask(#dto.taskId)")
    public void deleteComment(@Valid @RequestBody DeleteCommentDTO dto) {
        commentService.deleteCommentById(dto);
    }
}
