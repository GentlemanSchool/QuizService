package ru.gentleman.quiz.dto;

import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

public record QuizAttemptDto(
        UUID id,

        UUID userId,
        UUID quizId,

        Integer score,

        LocalDateTime createdAt,

        LocalDateTime completedAt
) {
}
