package com.system.tm.web.controller.confidential;

import com.system.tm.aspect.log.LogInfo;
import com.system.tm.service.ITaskService;
import com.system.tm.validator.marker.OnCreate;
import com.system.tm.validator.marker.OnUpdate;
import com.system.tm.web.dto.task.RequestTaskDTO;
import com.system.tm.web.dto.task.ResponseTaskDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "ADMIN TASK Controller",
        description = "CRUD OPERATIONS WITH TASKS"
)
@RestController
@RequestMapping(value = "/api/v1/admin/task")
@RequiredArgsConstructor
@LogInfo
public class PrivateTaskController {

    private final ITaskService taskService;

    @PostMapping
    @Operation(summary = "Создание задачи")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseTaskDTO createTask(@Validated(OnCreate.class) @RequestBody RequestTaskDTO taskDTO) {
        return taskService.createTask(
                taskDTO
        );
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Редактировать задачу")
    public ResponseTaskDTO updateTask(@Validated(OnUpdate.class) @RequestBody RequestTaskDTO vacancyDTO ){
        return taskService.updateTask(vacancyDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить задачу по ID")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable @Min(1) Long id) {
        taskService.deleteTaskById(id);
    }

}
