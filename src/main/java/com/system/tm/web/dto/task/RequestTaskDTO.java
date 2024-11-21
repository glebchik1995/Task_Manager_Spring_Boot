package com.system.tm.web.dto.task;

import com.system.tm.service.model.task.Priority;
import com.system.tm.service.model.task.Status;
import com.system.tm.validator.enums.EnumAllowedConstraint;
import com.system.tm.validator.marker.OnCreate;
import com.system.tm.validator.marker.OnUpdate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.lang.Nullable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(of = "id")
public class RequestTaskDTO {

    @Min(groups = OnUpdate.class, value = 1)
    private Long id;

    @NotBlank(groups = OnCreate.class)
    private String title;

    @NotBlank(groups = OnCreate.class)
    private String description;

    @NotNull(groups = OnCreate.class)
    @EnumAllowedConstraint(
            enumClass = Status.class,
            allowed =
                    {
                            "PENDING",
                            "IN_PROGRESS",
                            "COMPLETED"
                    }
    )
    private Status status;

    @NotNull(groups = OnCreate.class)
    @EnumAllowedConstraint(
            enumClass = Status.class,
            allowed =
                    {
                            "HIGH",
                            "MEDIUM",
                            "LOW"
                    }
    )
    private Priority priority;

    @NotBlank(groups = OnCreate.class)
    @Email
    private String authorEmail; // email автора

    @Nullable
    @Email
    private String assigneeEmail; // email исполнителя
}
