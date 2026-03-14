package ru.gentleman.quiz.query.handler;

import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;
import ru.gentleman.quiz.dto.QuestionDto;
import ru.gentleman.quiz.dto.QuizDto;
import ru.gentleman.quiz.query.FindAllQuestionsByQuizIdQuery;
import ru.gentleman.quiz.query.FindAllQuizzesByLessonIdQuery;
import ru.gentleman.quiz.query.FindQuizByIdQuery;
import ru.gentleman.quiz.service.QuizService;

import java.util.List;

@Component
@RequiredArgsConstructor
public class QuizQueryHandler {

    private final QuizService quizService;

    @QueryHandler
    public QuizDto on(FindQuizByIdQuery query) {
        return this.quizService.get(query.id());
    }

    @QueryHandler
    public List<QuizDto> on(FindAllQuizzesByLessonIdQuery query) {
        return this.quizService.getAllByLessonId(query.lessonId());
    }

    @QueryHandler
    public List<QuestionDto> on(FindAllQuestionsByQuizIdQuery query) {
        return this.quizService.getAllQuestions(query.quizId());
    }
}
