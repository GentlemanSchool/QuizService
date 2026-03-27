package ru.gentleman.quiz.command;

import lombok.Builder;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.time.Instant;
import java.util.UUID;

@Builder
public record CreateUserAnswerCommand(
        UUID id,
        UUID userId,
        UUID questionId,
        @TargetAggregateIdentifier
        UUID quizAttemptId,
        String answer,
        Instant createdAt
) {
}
