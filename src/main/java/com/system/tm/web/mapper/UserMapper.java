package com.system.tm.web.mapper;

import com.system.tm.service.model.user.User;
import com.system.tm.web.dto.user.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "passwordConfirmation", ignore = true)
    User toEntity(UserDTO userDTO);

    UserDTO toDto(User user);
}
