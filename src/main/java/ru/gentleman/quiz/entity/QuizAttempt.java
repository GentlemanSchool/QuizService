package ru.gentleman.quiz.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(schema = "quiz", name = "quiz_attempts")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuizAttempt {

    @Id
    private UUID id;

    private UUID userId;

    private int finalScore;

    private Instant createdAt;

    private Instant completedAt;

    private Boolean isActive;

    @ManyToOne
    @JoinColumn(name = "quiz_id", referencedColumnName = "id")
    private Quiz quiz;

    @OneToMany(mappedBy = "quizAttempt")
    private List<UserAnswer> userAnswers;
}
