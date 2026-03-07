package ru.gentleman.quiz.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(schema = "quiz", name = "quiz_attempts")
@AllArgsConstructor
@NoArgsConstructor
public class QuizAttempt {

    @Id
    @UuidGenerator
    @GeneratedValue
    private UUID id;

    private UUID userId;

    private Integer score;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private LocalDateTime completedAt;

    @ManyToOne
    @JoinColumn(name = "quiz_id", referencedColumnName = "id")
    private Quiz quiz;

    @OneToMany(mappedBy = "quizAttempt")
    private List<UserAnswer> userAnswers;
}
