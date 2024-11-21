package com.system.tm.service;

import com.system.tm.repositoty.CommentRepository;
import com.system.tm.repositoty.TaskRepository;
import com.system.tm.repositoty.exception.DataNotFoundException;
import com.system.tm.service.impl.CommentService;
import com.system.tm.service.model.comment.Comment;
import com.system.tm.service.model.task.Task;
import com.system.tm.web.dto.comment.RequestCommentDTO;
import com.system.tm.web.dto.comment.ResponseCommentDTO;
import com.system.tm.web.mapper.CommentMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private CommentService commentService;


    @Test
    void createComment_Success() {
        RequestCommentDTO dto = new RequestCommentDTO();
        dto.setTaskId(1L);
        dto.setText("comment");

        Task task = new Task();
        task.setId(1L);
        task.setAssigneeEmail("test@example.com");
        Comment comment = new Comment();
        comment.setText("comment");
        comment.setAssigneeEmail(task.getAssigneeEmail());

        when(taskRepository.findById(dto.getTaskId())).thenReturn(Optional.of(task));
        when(commentMapper.toEntity(dto)).thenReturn(comment);
        when(commentRepository.save(comment)).thenReturn(comment);
        when(commentMapper.toDTO(comment)).thenReturn(new ResponseCommentDTO());

        ResponseCommentDTO result = commentService.createComment(dto);
        assertNotNull(result);

        verify(commentRepository).save(comment);
    }


    @Test
    void getCommentById_Success() {
        Long id = 1L;
        Comment comment = new Comment();
        comment.setId(id);

        when(commentRepository.findById(id)).thenReturn(Optional.of(comment));
        when(commentMapper.toDTO(comment)).thenReturn(new ResponseCommentDTO());

        ResponseCommentDTO result = commentService.getCommentById(id);
        assertNotNull(result);
        verify(commentRepository).findById(id);
    }


    @Test
    void editComment_Success() {
        LocalDateTime now = LocalDateTime.now();
        RequestCommentDTO dto = new RequestCommentDTO();
        dto.setId(1L);
        dto.setText("Измененный текст");
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setCreatedAt(now.minusHours(1).minusMinutes(59));
        when(commentRepository.findById(dto.getId())).thenReturn(Optional.of(comment));
        when(commentMapper.toDTO(any(Comment.class))).thenReturn(new ResponseCommentDTO());

        ResponseCommentDTO result = commentService.editComment(dto);

        assertNotNull(result);
        verify(commentRepository, never()).delete(comment);
    }

    @Test
    void createComment_TaskNotFound() {
        RequestCommentDTO dto = new RequestCommentDTO();
        dto.setTaskId(1L);

        when(taskRepository.findById(dto.getTaskId())).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> commentService.createComment(dto));
    }


}
