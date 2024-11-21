package com.system.tm.web.dto.task;

import com.system.tm.service.model.task.Priority;
import com.system.tm.service.model.task.Status;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ResponseTaskDTO {

    private Long id;
    private String title;
    private String description;
    private Status status;
    private Priority priority;
    private String authorEmail;
    private String assigneeEmail;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
