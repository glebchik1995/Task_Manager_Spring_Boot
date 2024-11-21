package com.system.tm.service;

import com.system.tm.repositoty.UserRepository;
import com.system.tm.repositoty.exception.DataNotFoundException;
import com.system.tm.service.impl.AuthService;
import com.system.tm.service.model.user.Role;
import com.system.tm.service.model.user.User;
import com.system.tm.web.dto.auth.JwtRequest;
import com.system.tm.web.dto.auth.JwtResponse;
import com.system.tm.web.security.JwtTokenProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Optional;

import static com.system.tm.service.model.user.Role.USER;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider tokenProvider;

    @InjectMocks
    private AuthService authenticationService;

    @Test
    void login() {
        Long userId = 1L;
        String username = "username";
        String password = "password";
        Role role = USER;
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";
        JwtRequest request = new JwtRequest();
        request.setUsername(username);
        request.setPassword(password);
        User user = new User();
        user.setId(userId);
        user.setUsername(username);
        user.setRole(role);

        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        Mockito.when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        Mockito.when(tokenProvider.createAccessToken(userId, username, role)).thenReturn(accessToken);
        Mockito.when(tokenProvider.createRefreshToken(userId, username)).thenReturn(refreshToken);

        JwtResponse response = authenticationService.login(request);

        Mockito.verify(authenticationManager)
                .authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getUsername(),
                                request.getPassword())
                );
        Assertions.assertEquals(response.getUsername(), username);
        Assertions.assertEquals(response.getId(), userId);
        Assertions.assertEquals(response.getAccessToken(), accessToken);
        Assertions.assertEquals(response.getRefreshToken(), refreshToken);
    }

    @Test
    void loginWithIncorrectUsername() {
        String username = "username";
        String password = "password";
        JwtRequest request = new JwtRequest();
        request.setUsername(username);
        request.setPassword(password);
        Assertions.assertThrows(DataNotFoundException.class,
                () -> authenticationService.login(request));
    }

    @Test
    void refresh() {
        String refreshToken = "refreshToken";
        String accessToken = "accessToken";
        String newRefreshToken = "newRefreshToken";
        JwtResponse response = new JwtResponse();
        response.setAccessToken(accessToken);
        response.setRefreshToken(newRefreshToken);
        Mockito.when(tokenProvider.refreshUserTokens(refreshToken))
                .thenReturn(response);
        JwtResponse testResponse = authenticationService.refresh(refreshToken);
        Mockito.verify(tokenProvider).refreshUserTokens(refreshToken);
        Assertions.assertEquals(testResponse, response);
    }

}
