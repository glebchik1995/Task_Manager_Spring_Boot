package com.system.tm.web.security;

import com.system.tm.repositoty.UserRepository;
import com.system.tm.repositoty.exception.DataNotFoundException;
import com.system.tm.service.model.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(final String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new DataNotFoundException("Пользователь не найден"));
        return JwtEntityFactory.create(user);
    }

}
