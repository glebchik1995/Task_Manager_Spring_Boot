package com.system.tm.web.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Schema(description = "Обновление токена")
@NoArgsConstructor
@Getter
@Setter
public class RefreshTokenRequest {

    @NotNull(message = "Токен не может быть пустыми")
    private String refreshToken;
}

