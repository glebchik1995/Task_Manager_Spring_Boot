package com.system.tm.config;

import com.system.tm.web.security.JwtTokenFilter;
import com.system.tm.web.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.system.tm.service.model.user.Role.ADMIN;
import static com.system.tm.service.model.user.Role.USER;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class SecurityConfiguration {
    private final JwtTokenProvider tokenProvider;

    /**
     * Создает и возвращает экземпляр кодировщика паролей BCrypt.
     *
     * @return Кодировщик паролей.
     */

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Получает и возвращает менеджер аутентификации из конфигурации аутентификации.
     *
     * @param configuration Конфигурация аутентификации.
     * @return Менеджер аутентификации.
     */

    @Bean
    @SneakyThrows
    public AuthenticationManager authenticationManager(final AuthenticationConfiguration configuration) {
        return configuration.getAuthenticationManager();
    }

    /**
     * Настраивает цепочку фильтров безопасности для обработки HTTP-запросов.
     *
     * @param httpSecurity Объект HttpSecurity для настройки безопасности.
     * @return Цепочка фильтров безопасности.
     */

    @Bean
    @SneakyThrows
    public SecurityFilterChain filterChain(final HttpSecurity httpSecurity) {

        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement ->
                        sessionManagement
                                .sessionCreationPolicy(
                                        SessionCreationPolicy.STATELESS
                                )
                )
                .exceptionHandling(configurer ->
                        configurer.authenticationEntryPoint(
                                        (request, response, exception) -> {
                                            response.setStatus(
                                                    HttpStatus.UNAUTHORIZED
                                                            .value()
                                            );
                                            response.getWriter()
                                                    .write("UNAUTHORIZED.");
                                        })
                                .accessDeniedHandler(
                                        (request, response, exception) -> {
                                            response.setStatus(
                                                    HttpStatus.FORBIDDEN
                                                            .value()
                                            );
                                            response.getWriter()
                                                    .write("FORBIDDEN.");
                                        }))
                .authorizeHttpRequests(configurer ->
                        configurer
                                .requestMatchers("/api/v1/auth/**")
                                .permitAll()
                                .requestMatchers(
                                        "/swagger-ui/**",
                                        "/swagger-resources/*",
                                        "/v3/api-docs/**"
                                )
                                .permitAll()
                                .requestMatchers("/api/v1/admin/**").hasAnyAuthority(
                                        String.valueOf(ADMIN)
                                )
                                .requestMatchers("/api/v1/comment/**").hasAnyAuthority(
                                        String.valueOf(USER),
                                        String.valueOf(ADMIN)
                                )
                                .requestMatchers("/api/v1/task/**").hasAnyAuthority(
                                        String.valueOf(USER),
                                        String.valueOf(ADMIN)
                                )
                                .anyRequest().authenticated()
                )
                .anonymous(AbstractHttpConfigurer::disable)
                .addFilterBefore(new JwtTokenFilter(tokenProvider),
                        UsernamePasswordAuthenticationFilter.class)
        ;

        return httpSecurity.build();
    }
}
