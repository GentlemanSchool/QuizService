package ru.gentleman.quiz.dto;

import java.util.UUID;

public record QuizDto(
        UUID id,

        String title,

        UUID lessonId
) {
}
