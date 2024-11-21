package com.system.tm.web.dto.task;

import com.system.tm.service.model.task.Status;
import com.system.tm.validator.enums.EnumAllowedConstraint;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(of = "id")
public class ChangeTaskStatusDTO {

    @Min(value = 1)
    private Long id;

    @NotNull
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
}
