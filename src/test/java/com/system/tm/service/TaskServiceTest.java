package com.system.tm.service;

import com.system.tm.repositoty.TaskRepository;
import com.system.tm.repositoty.exception.DataNotFoundException;
import com.system.tm.service.impl.TaskService;
import com.system.tm.service.model.task.Task;
import com.system.tm.web.dto.task.ChangeTaskStatusDTO;
import com.system.tm.web.dto.task.ResponseTaskDTO;
import com.system.tm.web.mapper.TaskMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.system.tm.service.model.task.Status.PENDING;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskService taskService;

    @Test
    void getTaskById_Success() {
        Long taskId = 1L;
        Task task = new Task();
        task.setId(taskId);
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskMapper.toDTO(task)).thenReturn(new ResponseTaskDTO());

        ResponseTaskDTO result = taskService.getTaskById(taskId);
        assertNotNull(result);
        verify(taskRepository).findById(taskId);
    }


    @Test
    void getTaskById_NotFound() {
        Long taskId = 1L;
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> taskService.getTaskById(taskId));
        verify(taskRepository).findById(taskId);

    }

    @Test
    void updateStatusTaskByAssignee_Success() {
        Long id = 1L;
        ChangeTaskStatusDTO dto = new ChangeTaskStatusDTO();
        dto.setId(id);
        dto.setStatus(PENDING);
        Task task = new Task();
        task.setId(id);
        when(taskRepository.findById(id)).thenReturn(Optional.of(task));

        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toDTO(task)).thenReturn(new ResponseTaskDTO());

        ResponseTaskDTO result = taskService.updateStatusTaskByAssignee(dto);
        assertNotNull(result);
        verify(taskRepository).save(task);
    }

    @Test
    void updateStatusTaskByAssignee_NotFound() {
        Long id = 1L;
        ChangeTaskStatusDTO dto = new ChangeTaskStatusDTO();
        dto.setId(id);
        when(taskRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> taskService.updateStatusTaskByAssignee(dto));
    }
}
