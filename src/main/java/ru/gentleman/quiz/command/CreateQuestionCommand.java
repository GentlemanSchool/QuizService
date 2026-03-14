package ru.gentleman.quiz.command;

import lombok.Builder;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;

@Builder
public record CreateQuestionCommand(
        UUID id,
        @TargetAggregateIdentifier
        UUID quizId,
        String title,
        String description,
        String correctAnswer,
        String explanation,
        int score
) {
}
