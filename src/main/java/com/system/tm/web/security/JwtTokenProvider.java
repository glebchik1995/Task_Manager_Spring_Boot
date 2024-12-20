package com.system.tm.web.security;

import com.system.tm.aspect.log.LogError;
import com.system.tm.repositoty.UserRepository;
import com.system.tm.repositoty.exception.DataAccessException;
import com.system.tm.repositoty.exception.DataNotFoundException;
import com.system.tm.service.model.user.Role;
import com.system.tm.service.model.user.User;
import com.system.tm.service.properties.JwtProperties;
import com.system.tm.web.dto.auth.JwtResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@RequiredArgsConstructor
@LogError
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;

    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private SecretKey key;

    @PostConstruct
    public void init() {
        this.key = generateSecureKey();
    }

    private SecretKey generateSecureKey() {
        byte[] keyBytes = new byte[32];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(keyBytes);
        return new SecretKeySpec(keyBytes, "HmacSHA256");
    }

    public String createAccessToken(
            final Long userId,
            final String username,
            final Role role
    ) {
        Claims claims = Jwts.claims()
                .subject(username)
                .add("id", userId)
                .add("role", role)
                .build();
        Instant validity = Instant.now()
                .plus(jwtProperties.getAccess(), ChronoUnit.HOURS);
        return Jwts.builder()
                .claims(claims)
                .expiration(Date.from(validity))
                .signWith(key)
                .compact();
    }

    public String createRefreshToken(
            final Long userId,
            final String username
    ) {
        Claims claims = Jwts.claims()
                .subject(username)
                .add("id", userId)
                .build();
        Instant validity = Instant.now()
                .plus(jwtProperties.getRefresh(), ChronoUnit.DAYS);
        return Jwts.builder()
                .claims(claims)
                .expiration(Date.from(validity))
                .signWith(key)
                .compact();
    }

    public JwtResponse refreshUserTokens(final String refreshToken) {
        JwtResponse jwtResponse = new JwtResponse();
        if (!isValid(refreshToken)) {
            throw new DataAccessException("Отказано в доступе");
        }
        Long userId = Long.valueOf(getId(refreshToken));
        User user = userRepository.findById(userId).orElseThrow(() -> new DataNotFoundException("Пользователь не найден"));
        jwtResponse.setId(userId);
        jwtResponse.setUsername(user.getUsername());
        jwtResponse.setAccessToken(createAccessToken(userId, user.getUsername(), user.getRole()));
        jwtResponse.setRefreshToken(createRefreshToken(userId, user.getUsername()));
        return jwtResponse;
    }

    public boolean isValid(final String token) {
        Jws<Claims> claims = Jwts
                .parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);
        return claims.getPayload()
                .getExpiration()
                .after(new Date());
    }

    private String getId(final String token) {
        return Jwts
                .parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("id", String.class);
    }

    private String getUsername(final String token) {
        return Jwts
                .parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public Authentication getAuthentication(final String token) {
        String username = getUsername(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(
                userDetails,
                "",
                userDetails.getAuthorities()
        );
    }

}
