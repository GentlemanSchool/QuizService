package ru.gentleman.quiz.query.handler;

import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;
import ru.gentleman.quiz.dto.QuizAttemptDto;
import ru.gentleman.quiz.query.FindAllQuizAttemptsByUserIdQuery;
import ru.gentleman.quiz.query.FindQuizAttemptByIdQuery;
import ru.gentleman.quiz.service.QuizAttemptService;

import java.util.List;

@Component
@RequiredArgsConstructor
public class QuizAttemptQueryHandler {

    private final QuizAttemptService quizAttemptService;

    @QueryHandler
    public QuizAttemptDto on(FindQuizAttemptByIdQuery query) {
        return this.quizAttemptService.get(query.id());
    }

    @QueryHandler
    public List<QuizAttemptDto> on(FindAllQuizAttemptsByUserIdQuery query) {
        return this.quizAttemptService.getAllByUserId(query.userId());
    }
}
