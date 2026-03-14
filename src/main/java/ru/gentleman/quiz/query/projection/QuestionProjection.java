package ru.gentleman.quiz.query.projection;

import lombok.RequiredArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;
import ru.gentleman.common.event.QuestionCreatedEvent;
import ru.gentleman.common.event.QuestionDeletedEvent;
import ru.gentleman.common.event.QuestionUpdatedEvent;
import ru.gentleman.quiz.dto.QuestionDto;
import ru.gentleman.quiz.service.QuestionService;

@Component
@RequiredArgsConstructor
@ProcessingGroup("question-group")
public class QuestionProjection {

    private final QuestionService questionService;

    @EventHandler
    public void on(QuestionCreatedEvent event) {
        QuestionDto questionDto = QuestionDto.builder()
                .id(event.id())
                .quizId(event.quizId())
                .title(event.title())
                .description(event.description())
                .correctAnswer(event.correctAnswer())
                .explanation(event.explanation())
                .score(event.score())
                .build();

        this.questionService.create(questionDto);
    }

    @EventHandler
    public void on(QuestionUpdatedEvent event) {
        QuestionDto questionDto = QuestionDto.builder()
                .id(event.id())
                .quizId(event.quizId())
                .title(event.title())
                .description(event.description())
                .correctAnswer(event.correctAnswer())
                .explanation(event.explanation())
                .score(event.score())
                .build();

        this.questionService.update(event.id(), questionDto);
    }

    @EventHandler
    public void on(QuestionDeletedEvent event) {
        this.questionService.delete(event.id());
    }
}
