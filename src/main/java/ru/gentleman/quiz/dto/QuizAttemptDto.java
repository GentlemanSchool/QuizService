package ru.gentleman.quiz.dto;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record QuizAttemptDto(
        UUID id,
        UUID userId,
        UUID quizId,
        int finalScore,
        Instant createdAt,
        Instant completedAt,
        Boolean isActive
) {
}
