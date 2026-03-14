package ru.gentleman.quiz.query.projection;

import lombok.RequiredArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;
import ru.gentleman.common.event.QuizCreatedEvent;
import ru.gentleman.common.event.QuizDeletedEvent;
import ru.gentleman.common.event.QuizUpdatedEvent;
import ru.gentleman.quiz.dto.QuizDto;
import ru.gentleman.quiz.service.QuizService;

@Component
@RequiredArgsConstructor
@ProcessingGroup("quiz-group")
public class QuizProjection {

    private final QuizService quizService;

    @EventHandler
    public void on(QuizCreatedEvent event) {
        QuizDto quizDto = QuizDto.builder()
                .id(event.id())
                .lessonId(event.lessonId())
                .title(event.title())
                .description(event.description())
                .build();

        this.quizService.create(quizDto);
    }

    @EventHandler
    public void on(QuizUpdatedEvent event) {
        QuizDto quizDto = QuizDto.builder()
                .id(event.id())
                .title(event.title())
                .description(event.description())
                .build();

        this.quizService.update(event.id(), quizDto);
    }

    @EventHandler
    public void on(QuizDeletedEvent event) {
        this.quizService.delete(event.id());
    }
}
