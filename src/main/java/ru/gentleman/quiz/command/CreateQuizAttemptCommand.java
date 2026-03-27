package ru.gentleman.quiz.command;

import lombok.Builder;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import ru.gentleman.quiz.dto.QuestionDto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
public record CreateQuizAttemptCommand(
        @TargetAggregateIdentifier
        UUID id,
        UUID userId,
        UUID quizId,
        Instant createdAt,
        List<QuestionDto> questions
) {
}
