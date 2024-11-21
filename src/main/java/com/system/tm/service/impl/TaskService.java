package com.system.tm.service.impl;

import com.system.tm.aspect.log.LogError;
import com.system.tm.repositoty.TaskRepository;
import com.system.tm.repositoty.UserRepository;
import com.system.tm.repositoty.exception.DataNotFoundException;
import com.system.tm.service.ITaskService;
import com.system.tm.service.filter.CriteriaModel;
import com.system.tm.service.filter.GenericSpecification;
import com.system.tm.service.filter.JoinType;
import com.system.tm.service.model.task.Task;
import com.system.tm.service.model.task.Task_;
import com.system.tm.service.model.user.User;
import com.system.tm.util.FilterParser;
import com.system.tm.util.NullPropertyCopyHelper;
import com.system.tm.web.dto.task.ChangeTaskStatusDTO;
import com.system.tm.web.dto.task.RequestTaskDTO;
import com.system.tm.web.dto.task.ResponseTaskDTO;
import com.system.tm.web.mapper.TaskMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@LogError
@RequiredArgsConstructor
public class TaskService implements ITaskService {
    private final TaskMapper taskMapper;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ResponseTaskDTO createTask(RequestTaskDTO candidateDTO) {
        Task task = taskMapper.toEntity(candidateDTO);
        return taskMapper.toDTO(taskRepository.save(task));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ResponseTaskDTO updateTask(RequestTaskDTO requestTaskDTO) {
        Task task = taskRepository.findById(requestTaskDTO.getId())
                .orElseThrow(() -> new DataNotFoundException("Задача не найдена"));
        NullPropertyCopyHelper.copyNonNullProperties(requestTaskDTO, task);
        Task updatedTask = taskRepository.save(task);
        return taskMapper.toDTO(updatedTask);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseTaskDTO getTaskById(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new DataNotFoundException("Задача не найдена"));
        return taskMapper.toDTO(task);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ResponseTaskDTO> getTasks(
            String email,
            String criteriaJson,
            JoinType joinType,
            Pageable pageable
    ) {
        User user = userRepository.findByUsername(email)
                .orElseThrow(() -> new DataNotFoundException("Пользователь не найден"));
        List<CriteriaModel> criteriaList = (criteriaJson != null)
                ? FilterParser.parseCriteriaJson(criteriaJson)
                : Collections.emptyList();
        Specification<Task> specification = createSpecification(user, criteriaList, joinType);
        return (specification != null)
                ? taskRepository.findAll(specification, pageable).map(taskMapper::toDTO)
                : taskRepository.findAll(pageable).map(taskMapper::toDTO);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ResponseTaskDTO updateStatusTaskByAssignee(ChangeTaskStatusDTO dto) {
        Task task = taskRepository.findById(dto.getId())
                .orElseThrow(() -> new DataNotFoundException("Задача не найдена"));
        task.setStatus(dto.getStatus());
        taskRepository.save(task);
        return taskMapper.toDTO(task);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteTaskById(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new DataNotFoundException("Задача не найдена"));
        taskRepository.delete(task);
    }

    private Specification<Task> createSpecification(
            User user,
            List<CriteriaModel> criteriaList,
            JoinType joinType
    ) {
        Specification<Task> baseSpecification = null;
        switch (user.getRole()) {
            case ADMIN:
                if (!criteriaList.isEmpty()) {
                    baseSpecification = new GenericSpecification<>(criteriaList, joinType, Task.class);
                }
                break;
            case USER:
                Specification<Task> assigneeSpec = (root, query, cb) ->
                        cb.equal(root.get(Task_.ASSIGNEE_EMAIL), user.getUsername());
                if (!criteriaList.isEmpty()) {
                    baseSpecification = new GenericSpecification<>(criteriaList, joinType, Task.class).and(assigneeSpec);
                } else {
                    baseSpecification = assigneeSpec;
                }
                break;
            default:
                break;
        }
        return baseSpecification;
    }
}
