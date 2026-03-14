package ru.gentleman.quiz.query.handler;

import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;
import ru.gentleman.quiz.dto.UserAnswerDto;
import ru.gentleman.quiz.query.FindAllUserAnswersByUserIdQuery;
import ru.gentleman.quiz.query.FindUserAnswerByIdQuery;
import ru.gentleman.quiz.service.UserAnswerService;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserAnswerQueryHandler {

    private final UserAnswerService userAnswerService;

    @QueryHandler
    public UserAnswerDto on(FindUserAnswerByIdQuery query) {
        return this.userAnswerService.get(query.id());
    }

    @QueryHandler
    public List<UserAnswerDto> on(FindAllUserAnswersByUserIdQuery query) {
        return this.userAnswerService.getAllByUserId(query.userId());
    }
}
