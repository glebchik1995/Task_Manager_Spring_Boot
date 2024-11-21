package com.system.tm.service.impl;

import com.system.tm.repositoty.UserRepository;
import com.system.tm.repositoty.exception.DataAlreadyExistException;
import com.system.tm.repositoty.exception.DataNotFoundException;
import com.system.tm.service.IUserService;
import com.system.tm.service.model.user.User;
import com.system.tm.web.dto.user.UserDTO;
import com.system.tm.web.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    /**
     * Создание пользователя
     *
     * @return созданный пользователь
     */
    @Override
    public UserDTO create(final UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new DataAlreadyExistException("Пользователь уже существует.");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(userDTO.getRole());
        return userMapper.toDto(userRepository.save(user));
    }

    public User getByUsername(final String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new DataNotFoundException("Пользователь не найден"));
    }
}
