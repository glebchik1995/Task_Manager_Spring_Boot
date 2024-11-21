package com.system.tm.web.security.expression;

import com.system.tm.repositoty.TaskRepository;
import com.system.tm.repositoty.exception.DataNotFoundException;
import com.system.tm.service.model.task.Task;
import com.system.tm.service.model.user.Role;
import com.system.tm.web.security.JwtEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("cse")
@RequiredArgsConstructor
@Slf4j
public class CustomSecurityExpression {

    private final TaskRepository taskRepository;


    public boolean canAccessUserTask(final Long id) {
        JwtEntity entity = this.getPrincipal();
        String loggedUsername = entity.getName();
        log.info("Пользователь {} пытается получить доступ к задаче с ID: {}", loggedUsername, id);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Задача не найдена"));
        boolean hasAccess = loggedUsername.equals(task.getAssigneeEmail()) || hasAnyRole(Role.ADMIN);
        log.info("Пользователь {} имеет доступ к задаче {}: {}", loggedUsername, id, hasAccess);
        return hasAccess;
    }

    public boolean canAccessUserComment(final Long id) {
        JwtEntity entity = this.getPrincipal();
        String loggedUsername = entity.getName();

        Task task  = taskRepository.findTaskByCommentId(id);

        return loggedUsername.equals(task.getAssigneeEmail()) || hasAnyRole(Role.ADMIN);
    }



    private boolean hasAnyRole(final Role... roles) {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        for (Role role : roles) {
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.name());
            if (authentication.getAuthorities().contains(authority)) {
                return true;
            }
        }
        return false;
    }

    private JwtEntity getPrincipal() {
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();
        return (JwtEntity) authentication.getPrincipal();
    }
}
