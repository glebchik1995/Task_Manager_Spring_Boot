package com.system.tm.service;

import com.system.tm.repositoty.UserRepository;
import com.system.tm.repositoty.exception.DataNotFoundException;
import com.system.tm.service.impl.UserService;
import com.system.tm.service.model.user.User;
import com.system.tm.web.dto.user.UserDTO;
import com.system.tm.web.mapper.UserMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;


    @Test
    void getByUsername() {
        String username = "username@gmail.com";
        User user = new User();
        user.setUsername(username);
        when(userRepository.findByUsername(username))
                .thenReturn(Optional.of(user));
        User testUser = userService.getByUsername(username);
        verify(userRepository).findByUsername(username);
        assertEquals(user, testUser);
    }

    @Test
    void getByNotExistingUsername() {
        String username = "username@gmail.com";
        when(userRepository.findByUsername(username))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(DataNotFoundException.class,
                () -> userService.getByUsername(username));
        verify(userRepository).findByUsername(username);
    }


    @Test
    void testCreateUser_Success() {
        UserDTO userDTO = UserDTO.builder()
                .name("name")
                .username("username@mail.com")
                .password("password")
                .passwordConfirmation("password")
                .build();

        User user = User.builder()
                .name("name")
                .username("username@mail.com")
                .password("password")
                .passwordConfirmation("password")
                .build();

        when(userRepository.findByUsername("username@mail.com")).thenReturn(Optional.empty());
        when(userMapper.toEntity(userDTO)).thenReturn(user);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDTO);

        UserDTO createdUser = userService.create(userDTO);

        assertNotNull(createdUser);
        assertEquals("username@mail.com", createdUser.getUsername()); // Исправлено!
        assertEquals("password", createdUser.getPassword());
        verify(userRepository).save(user); //Проверяем что save вызвался
    }

}
