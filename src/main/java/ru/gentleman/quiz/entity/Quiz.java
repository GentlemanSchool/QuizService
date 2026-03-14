package ru.gentleman.quiz.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(schema = "quiz", name = "quizzes")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Quiz {

    @Id
    private UUID id;

    private String title;

    private String description;

    private UUID lessonId;

    @OneToMany(mappedBy = "quiz")
    private List<Question> questions;

    private Boolean isActive;
}
