package com.system.tm.service.model.task;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.system.tm.service.model.comment.Comment;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "task")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder(toBuilder = true)
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private Status status; // "в ожидании", "в процессе", "завершено"

    @Enumerated(EnumType.STRING)
    private Priority priority; // "высокий", "средний", "низкий"

    private String authorEmail; // email автора

    private String assigneeEmail; // email исполнителя

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonManagedReference
    private List<Comment> comments;
}