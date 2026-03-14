package ru.gentleman.quiz.query.controller;

import lombok.RequiredArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.*;
import ru.gentleman.common.exception.NotFoundException;
import ru.gentleman.quiz.dto.QuizDto;
import ru.gentleman.quiz.query.FindAllQuestionsByQuizIdQuery;
import ru.gentleman.quiz.query.FindAllQuizzesByLessonIdQuery;
import ru.gentleman.quiz.query.FindQuizByIdQuery;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/quizzes")
@RequiredArgsConstructor
public class QuizQueryController {

    private final QueryGateway queryGateway;

    @GetMapping("/{id}")
    public QuizDto get(@PathVariable("id") UUID id) {
        QuizDto quizDto = this.queryGateway.query(new FindQuizByIdQuery(id),
                ResponseTypes.instanceOf(QuizDto.class)).join();

        if(quizDto == null) {
            throw new NotFoundException("error.quiz.not_found", id);
        }

        return quizDto;
    }

    @GetMapping(params = "lessonId")
    public List<Object> getAllByLessonId(@RequestParam("lessonId") UUID lessonId) {
        return this.queryGateway.query(new FindAllQuizzesByLessonIdQuery(lessonId),
                ResponseTypes.multipleInstancesOf(Object.class)).join();
    }

    @GetMapping("/{id}/questions")
    public List<Object> getAllQuestions(@PathVariable("id") UUID id) {
        return this.queryGateway.query(new FindAllQuestionsByQuizIdQuery(id),
                ResponseTypes.multipleInstancesOf(Object.class)).join();
    }
}
