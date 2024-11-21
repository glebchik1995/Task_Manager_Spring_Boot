package com.system.tm.web.mapper;

import com.system.tm.service.model.comment.Comment;
import com.system.tm.web.dto.comment.RequestCommentDTO;
import com.system.tm.web.dto.comment.ResponseCommentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "assigneeEmail", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "task", ignore = true)
    Comment toEntity(RequestCommentDTO taskDTO);

    ResponseCommentDTO toDTO(Comment comment);
}
