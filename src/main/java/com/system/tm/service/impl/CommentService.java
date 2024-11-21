package com.system.tm.service.impl;

import com.system.tm.aspect.log.LogError;
import com.system.tm.repositoty.CommentRepository;
import com.system.tm.repositoty.TaskRepository;
import com.system.tm.repositoty.exception.DataNotFoundException;
import com.system.tm.service.ICommentService;
import com.system.tm.service.filter.CriteriaModel;
import com.system.tm.service.filter.GenericSpecification;
import com.system.tm.service.filter.JoinType;
import com.system.tm.service.model.comment.Comment;
import com.system.tm.service.model.comment.Comment_;
import com.system.tm.service.model.task.Task;
import com.system.tm.service.model.task.Task_;
import com.system.tm.util.FilterParser;
import com.system.tm.web.dto.comment.DeleteCommentDTO;
import com.system.tm.web.dto.comment.RequestCommentDTO;
import com.system.tm.web.dto.comment.ResponseCommentDTO;
import com.system.tm.web.mapper.CommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@LogError
@RequiredArgsConstructor
public class CommentService implements ICommentService {

    private final CommentMapper commentMapper;

    private final CommentRepository commentRepository;

    private final TaskRepository taskRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ResponseCommentDTO createComment(RequestCommentDTO dto) {
        Task task = taskRepository.findById(dto.getTaskId())
                .orElseThrow(() -> new DataNotFoundException("Задача не найдена"));
        Comment comment = commentMapper.toEntity(dto);
        comment.setAssigneeEmail(task.getAssigneeEmail());
        comment.setTask(task);
        commentRepository.save(comment);
        return commentMapper.toDTO(comment);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ResponseCommentDTO> getAllCommentsByTaskId(
            Long taskId,
            String criteriaJson,
            JoinType joinType,
            Pageable pageable
    ) {
        List<CriteriaModel> criteriaList = List.of();
        if (criteriaJson != null) {
            criteriaList = FilterParser.parseCriteriaJson(criteriaJson);
        }
        if (!criteriaList.isEmpty()) {
            Specification<Comment> hrSp = (root, query, cb) ->
                    cb.equal(root.get(Comment_.task).get(Task_.id), taskId);
            return commentRepository.findAll(
                            new GenericSpecification<>(
                                    criteriaList,
                                    joinType,
                                    Comment.class
                            ).and(hrSp),
                            pageable
                    )
                    .map(commentMapper::toDTO);
        } else {
            return commentRepository.findAllByTaskId(taskId, pageable)
                    .map(commentMapper::toDTO);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseCommentDTO getCommentById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Комментарий не найден"));
        return commentMapper.toDTO(comment);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ResponseCommentDTO editComment(RequestCommentDTO dto) {
        Comment comment = commentRepository.findById(dto.getId())
                .orElseThrow(() -> new DataNotFoundException("Комментарий не найден"));
        LocalDateTime now = LocalDateTime.now();
        if (Duration.between(comment.getCreatedAt(), now).toHours() > 1) {
            throw new IllegalArgumentException("Невозможно изменить комментарий, прошло больше часа с момента публикации.");
        }
        comment.setText(dto.getText());
        return commentMapper.toDTO(comment);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteCommentById(DeleteCommentDTO dto) {
        Comment comment = commentRepository.findById(dto.getCommentId())
                .orElseThrow(() -> new DataNotFoundException("Комментарий не найден"));
        LocalDateTime now = LocalDateTime.now();
        if (Duration.between(comment.getCreatedAt(), now).toHours() > 1) {
            throw new IllegalArgumentException("Невозможно удалить комментарий, прошло больше часа с момента публикации.");
        }
        commentRepository.delete(comment);
    }
}
