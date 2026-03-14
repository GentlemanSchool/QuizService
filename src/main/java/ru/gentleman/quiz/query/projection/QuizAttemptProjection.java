package ru.gentleman.quiz.query.projection;

import lombok.RequiredArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;
import ru.gentleman.common.event.QuizAttemptCreatedEvent;
import ru.gentleman.common.event.QuizAttemptDeletedEvent;
import ru.gentleman.common.event.QuizAttemptFinishedEvent;
import ru.gentleman.quiz.dto.QuizAttemptDto;
import ru.gentleman.quiz.service.QuizAttemptService;

@Component
@RequiredArgsConstructor
@ProcessingGroup("quiz-attempt-group")
public class QuizAttemptProjection {

    private final QuizAttemptService quizAttemptService;

    @EventHandler
    public void on(QuizAttemptCreatedEvent event) {
        QuizAttemptDto quizAttemptDto = QuizAttemptDto.builder()
                .id(event.id())
                .quizId(event.quizId())
                .userId(event.userId())
                .createdAt(event.createdAt())
                .build();

        this.quizAttemptService.create(quizAttemptDto);
    }

    @EventHandler
    public void on(QuizAttemptFinishedEvent event) {
        this.quizAttemptService.finish(
                event.id(), event.finalScore(), event.completedAt()
        );
    }

    @EventHandler
    public void on(QuizAttemptDeletedEvent event) {
        this.quizAttemptService.delete(event.id());
    }
}
