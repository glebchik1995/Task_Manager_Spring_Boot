package com.system.tm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.system.tm.BaseIntegrationTest;
import com.system.tm.repositoty.TaskRepository;
import com.system.tm.service.ITaskService;
import com.system.tm.service.model.task.Priority;
import com.system.tm.service.model.task.Status;
import com.system.tm.service.model.task.Task;
import com.system.tm.web.dto.task.RequestTaskDTO;
import com.system.tm.web.dto.task.ResponseTaskDTO;
import com.system.tm.web.mapper.TaskMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WithMockUser(username = "admin@mail.ru", authorities = {"ADMIN"})
public class TaskControllerTest extends BaseIntegrationTest {

    @Autowired(required = false)
    private MockMvc mvc;

    private final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

    @Autowired
    private ITaskService taskService;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskMapper taskMapper;

    @Test
    @DisplayName("Написать комментарий к задаче")
    void shouldCreateTaskTest() throws Exception {
        Task task = Task.builder()
                .title("Новая задача")
                .description("Описание новой задачи")
                .status(Status.PENDING)
                .priority(Priority.HIGH)
                .authorEmail("author@example.com")
                .assigneeEmail("assignee@example.com")
                .build();

        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders.post("/api/v1/admin/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(task))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn().getResponse();

        response.setCharacterEncoding("UTF-8");

        ResponseTaskDTO dto = mapper.readValue(response.getContentAsString(), ResponseTaskDTO.class);

        Assertions.assertEquals(task.getTitle(), dto.getTitle());
        Assertions.assertEquals(task.getDescription(), dto.getDescription());
        Assertions.assertEquals(task.getStatus(), dto.getStatus());
        Assertions.assertEquals(task.getPriority(), dto.getPriority());
        Assertions.assertEquals(task.getAuthorEmail(), dto.getAuthorEmail());
        Assertions.assertEquals(task.getAssigneeEmail(), dto.getAssigneeEmail());
        Assertions.assertEquals(mapper.writeValueAsString(dto), response.getContentAsString());
    }

    @Test
    @DisplayName("Обновить задачу")
    void shouldUpdateTask() throws Exception {

        Task existingTask = Task.builder()
                .title("Старая задача")
                .description("Старое описание")
                .status(Status.PENDING)
                .priority(Priority.HIGH)
                .authorEmail("author@example.com")
                .assigneeEmail("assignee@example.com")
                .build();
        taskRepository.save(existingTask);

        RequestTaskDTO requestTaskDTO = new RequestTaskDTO();
        requestTaskDTO.setId(existingTask.getId());
        requestTaskDTO.setTitle("Обновленная задача");
        requestTaskDTO.setDescription("Обновленное описание");

        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders.put("/api/v1/admin/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestTaskDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        response.setCharacterEncoding("UTF-8");

        ResponseTaskDTO updatedTask = mapper.readValue(response.getContentAsString(), ResponseTaskDTO.class);
        Assertions.assertEquals("Обновленная задача", updatedTask.getTitle());
        Assertions.assertEquals("Обновленное описание", updatedTask.getDescription());
        Assertions.assertEquals(mapper.writeValueAsString(updatedTask), response.getContentAsString());

    }

    @Test
    @DisplayName("Удалить задачу по ID")
    void shouldDeleteTaskById() throws Exception {

        Task task = Task.builder()
                .title("Тестовая задача")
                .description("Описание тестовой задачи")
                .status(Status.PENDING)
                .priority(Priority.HIGH)
                .authorEmail("author@example.com")
                .assigneeEmail("assignee@example.com")
                .build();
        taskRepository.save(task);

        mvc.perform(MockMvcRequestBuilders.delete("/api/v1/admin/task/" + task.getId()))
                .andExpect(status().isNoContent());

        Assertions.assertFalse(taskRepository.findById(task.getId()).isPresent());
    }
}
