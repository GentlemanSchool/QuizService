package ru.gentleman.quiz.query.projection;

import lombok.RequiredArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;
import ru.gentleman.common.event.UserAnswerCreatedEvent;
import ru.gentleman.quiz.dto.UserAnswerDto;
import ru.gentleman.quiz.service.UserAnswerService;

@Component
@RequiredArgsConstructor
@ProcessingGroup("user-answer-group")
public class UserAnswerProjection {

    private final UserAnswerService userAnswerService;

    @EventHandler
    public void on(UserAnswerCreatedEvent event) {
        UserAnswerDto userAnswerDto = UserAnswerDto.builder()
                .id(event.id())
                .userId(event.userId())
                .quizAttemptId(event.quizAttemptId())
                .questionId(event.questionId())
                .isCorrect(event.isCorrect())
                .createdAt(event.createdAt())
                .answer(event.answer())
                .build();

        this.userAnswerService.create(userAnswerDto);
    }
}
