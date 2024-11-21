package com.system.tm.service;

import com.system.tm.web.dto.auth.JwtRequest;
import com.system.tm.web.dto.auth.JwtResponse;

public interface IAuthService {

    JwtResponse login(
            JwtRequest loginRequest
    );

    JwtResponse refresh(
            String refreshToken
    );

}