package com.system.tm.web.dto.comment;

import jakarta.validation.constraints.Min;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class DeleteCommentDTO {

    @Min(value = 1)
    private Long commentId;

    @Min(value = 1)
    private Long taskId;
}
