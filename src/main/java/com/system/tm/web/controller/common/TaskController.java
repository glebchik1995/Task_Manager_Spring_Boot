package com.system.tm.web.controller.common;

import com.system.tm.aspect.log.LogInfo;
import com.system.tm.service.ITaskService;
import com.system.tm.service.filter.JoinType;
import com.system.tm.web.dto.task.ChangeTaskStatusDTO;
import com.system.tm.web.dto.task.ResponseTaskDTO;
import com.system.tm.web.security.JwtEntity;
import com.system.tm.web.security.expression.CustomSecurityExpression;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "TASK Controller",
        description = "CRUD OPERATIONS WITH TASKS"
)
@RestController
@RequestMapping(value = "/api/v1/task")
@RequiredArgsConstructor
@LogInfo
public class TaskController {

    private final ITaskService taskService;

    private final CustomSecurityExpression securityExpression;

    @GetMapping
    @Operation(summary = "Получить список задач")
    public Page<ResponseTaskDTO> getAllTasks(
            @AuthenticationPrincipal JwtEntity currentUser,
            @RequestParam(required = false) String criteriaJson,
            @RequestParam(required = false) JoinType joinType,
            @ParameterObject Pageable pageable
    ) {
        return taskService.getTasks(
                currentUser.getName(),
                criteriaJson,
                joinType,
                pageable
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить задачу по ID")
//    @PreAuthorize("@cse.canAccessUserTask(#id)")
    public ResponseTaskDTO getTaskById(@PathVariable @Min(1) Long id) {
        securityExpression.canAccessUserTask(id);
        return taskService.getTaskById(id);
    }

    @PutMapping
    @Operation(summary = "Изменить статус задачи у текущего исполнителя")
    @PreAuthorize("@cse.canAccessUserTask(#dto.id)")
    public ResponseTaskDTO updateStatusTaskByAssignee(@RequestBody @Valid ChangeTaskStatusDTO dto) {
        return taskService.updateStatusTaskByAssignee(dto);
    }
}
