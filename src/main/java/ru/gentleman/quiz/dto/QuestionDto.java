package ru.gentleman.quiz.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record QuestionDto(
        UUID id,
        UUID quizId,
        String title,
        String description,
        String correctAnswer,
        int score,
        String explanation,
        Boolean isActive
) {
}
