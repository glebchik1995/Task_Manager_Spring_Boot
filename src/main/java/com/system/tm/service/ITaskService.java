package com.system.tm.service;

import com.system.tm.service.filter.JoinType;
import com.system.tm.web.dto.task.ChangeTaskStatusDTO;
import com.system.tm.web.dto.task.RequestTaskDTO;
import com.system.tm.web.dto.task.ResponseTaskDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ITaskService {

    ResponseTaskDTO createTask(RequestTaskDTO candidateDTO);


    ResponseTaskDTO updateTask(RequestTaskDTO candidateDTO);

    ResponseTaskDTO getTaskById(
            Long taskId
    );

    Page<ResponseTaskDTO> getTasks(
            String email,
            String criteriaJson,
            JoinType joinType,
            Pageable pageable
    );

    ResponseTaskDTO updateStatusTaskByAssignee(ChangeTaskStatusDTO dto);

    void deleteTaskById(Long taskId);
}
