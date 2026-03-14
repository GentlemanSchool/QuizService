package ru.gentleman.quiz.query.handler;

import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;
import ru.gentleman.quiz.dto.QuestionDto;
import ru.gentleman.quiz.query.FindQuestionByIdQuery;
import ru.gentleman.quiz.service.QuestionService;

@Component
@RequiredArgsConstructor
public class QuestionQueryHandler {

    private final QuestionService questionService;

    @QueryHandler
    public QuestionDto on(FindQuestionByIdQuery query) {
        return this.questionService.get(query.id());
    }
}
