package ru.gentleman.quiz.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserAnswerDto(
        UUID id,

        UUID userId,
        UUID questionId,
        UUID quizAttemptId,

        String answer,

        LocalDateTime createdAt
) {
}
