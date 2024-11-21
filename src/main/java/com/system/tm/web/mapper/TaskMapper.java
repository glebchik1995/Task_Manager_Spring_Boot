package com.system.tm.web.mapper;

import com.system.tm.service.model.task.Task;
import com.system.tm.web.dto.task.RequestTaskDTO;
import com.system.tm.web.dto.task.ResponseTaskDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "comments", ignore = true)
    Task toEntity(RequestTaskDTO taskDTO);

    ResponseTaskDTO toDTO(Task task);
}
