package ru.gentleman.quiz.dto;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record UserAnswerDto(
        UUID id,
        UUID userId,
        UUID questionId,
        UUID quizAttemptId,
        Boolean isCorrect,
        String answer,
        Instant createdAt
) {
}
