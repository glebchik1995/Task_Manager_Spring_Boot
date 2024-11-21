package com.system.tm.web.controller;

import com.system.tm.aspect.log.LogInfo;
import com.system.tm.service.IAuthService;
import com.system.tm.service.IUserService;
import com.system.tm.web.dto.auth.JwtRequest;
import com.system.tm.web.dto.auth.JwtResponse;
import com.system.tm.web.dto.user.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Auth Controller",
        description = "Auth API"
)
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@LogInfo
@Validated
public class AuthController {

    private final IAuthService authService;
    private final IUserService userService;

    @PostMapping("/login")
    @Operation(summary = "Авторизоваться")
    public JwtResponse login(@Valid @RequestBody final JwtRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Зарегистрироваться")
    public UserDTO registration(@Valid @RequestBody final UserDTO userDto) {
        return userService.create(userDto);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Обновить токен")
    public JwtResponse refresh(@RequestBody final String refreshToken) {
        return authService.refresh(refreshToken);
    }

}
