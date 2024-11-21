package com.system.tm.web.dto.comment;

import com.system.tm.validator.marker.OnCreate;
import com.system.tm.validator.marker.OnUpdate;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(of = "id")
public class RequestCommentDTO {

    @Min(value = 1, groups = {OnUpdate.class})
    private Long id;

    @Min(value = 1, groups = {OnCreate.class})
    private Long taskId;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    private String text;
}
