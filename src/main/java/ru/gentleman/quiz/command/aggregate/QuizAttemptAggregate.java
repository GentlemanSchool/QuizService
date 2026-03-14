package ru.gentleman.quiz.command.aggregate;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandExecutionException;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import ru.gentleman.common.dto.QuestionSnapshot;
import ru.gentleman.common.event.QuizAttemptCreatedEvent;
import ru.gentleman.common.event.QuizAttemptFinishedEvent;
import ru.gentleman.common.event.QuizDeletedEvent;
import ru.gentleman.common.event.UserAnswerCreatedEvent;
import ru.gentleman.quiz.command.CreateQuizAttemptCommand;
import ru.gentleman.quiz.command.CreateUserAnswerCommand;
import ru.gentleman.quiz.command.DeleteQuizAttemptCommand;
import ru.gentleman.quiz.command.FinishQuizAttemptCommand;
import ru.gentleman.quiz.dto.QuestionDto;
import ru.gentleman.quiz.dto.UserAnswerDto;
import ru.gentleman.quiz.service.QuizService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Aggregate
@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class QuizAttemptAggregate {

    @AggregateIdentifier
    private UUID id;

    private UUID userId;

    private UUID quizId;

    private int totalPossiblePoints;

    private int currentEarnedPoints;

    private int finalScore;

    private Boolean isActive;

    private LocalDateTime createdAt;

    private LocalDateTime completedAt;

    private Map<UUID, QuestionSnapshot> questions;

    private List<UserAnswerDto> userAnswers;

    public QuizAttemptAggregate() {

    }

    @CommandHandler
    public QuizAttemptAggregate(CreateQuizAttemptCommand command, QuizService quizService) {
        List<QuestionDto> receivedQuestions = quizService.getAllQuestions(command.quizId());
        Map<UUID, QuestionSnapshot> questionMap = receivedQuestions.stream()
                .map(questionDto -> QuestionSnapshot.builder()
                        .id(questionDto.id())
                        .title(questionDto.title())
                        .description(questionDto.description())
                        .correctAnswer(questionDto.correctAnswer())
                        .explanation(questionDto.explanation())
                        .quizId(questionDto.quizId())
                        .score(questionDto.score())
                        .build())
                .collect(Collectors.toMap(
                        QuestionSnapshot::id,
                        Function.identity()
                ));
        int totalPossiblePoints = receivedQuestions.stream()
                .mapToInt(QuestionDto::score)
                .sum();

        QuizAttemptCreatedEvent event = QuizAttemptCreatedEvent.builder()
                .id(command.id())
                .totalPossiblePoints(totalPossiblePoints)
                .quizId(command.quizId())
                .userId(command.userId())
                .createdAt(command.createdAt())
                .questions(questionMap)
                .build();

        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(QuizAttemptCreatedEvent event) {
        this.id = event.id();
        this.userId = event.userId();
        this.quizId = event.quizId();
        this.totalPossiblePoints = event.totalPossiblePoints();
        this.createdAt = event.createdAt();
        this.userAnswers = new ArrayList<>();
        this.questions = event.questions();
        this.isActive = true;
    }

    @CommandHandler
    public void handle(CreateUserAnswerCommand command) {
        isQuizAttemptActive();

        if(this.completedAt != null) {
            throw new CommandExecutionException(
                    "error.quiz_attempt.already_finished",
                    null,
                    quizId
            );
        }

        QuestionSnapshot question = this.questions.get(command.questionId());

        if(question == null) {
            throw new CommandExecutionException(
                    "error.question.not_found",
                    null,
                    command.questionId()
            );
        }

        boolean alreadyAnswered = userAnswers.stream()
                .anyMatch(a -> a.questionId().equals(command.questionId()));

        if(alreadyAnswered) {
            throw new CommandExecutionException(
                    "error.user_answer.already_answered",
                    null,
                    command.quizAttemptId()
            );
        }

        boolean isCorrect = question.correctAnswer().equals(command.answer());

        UserAnswerCreatedEvent event = UserAnswerCreatedEvent.builder()
                .id(command.id())
                .answer(command.answer())
                .createdAt(command.createdAt())
                .isCorrect(isCorrect)
                .questionId(command.questionId())
                .quizAttemptId(command.quizAttemptId())
                .userId(command.userId())
                .build();
        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(UserAnswerCreatedEvent event) {
        UserAnswerDto userAnswer = UserAnswerDto.builder()
                .id(event.id())
                .answer(event.answer())
                .createdAt(event.createdAt())
                .isCorrect(event.isCorrect())
                .questionId(event.questionId())
                .quizAttemptId(event.quizAttemptId())
                .userId(event.userId())
                .build();
        this.userAnswers.add(userAnswer);

        if(event.isCorrect()) {
            QuestionSnapshot question = questions.get(event.questionId());
            currentEarnedPoints += question.score();
        }
    }

    @CommandHandler
    public void handle(FinishQuizAttemptCommand command) {
        isQuizAttemptActive();

        if(completedAt != null) {
            throw new CommandExecutionException(
                    "error.quiz_attempt.already_finished",
                    null,
                    quizId
            );
        }

        // Расчет процента
        double successPercentage = (double) this.currentEarnedPoints / this.totalPossiblePoints * 100;
        // Округляем до целого или до 2 знаков
        int finalScore = (int) Math.round(successPercentage);

        QuizAttemptFinishedEvent event = QuizAttemptFinishedEvent.builder()
                .id(command.id())
                .completedAt(LocalDateTime.now())
                .finalScore(finalScore)
                .build();

        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(QuizAttemptFinishedEvent event) {
        this.completedAt = event.completedAt();
        this.finalScore = event.finalScore();
    }

    @CommandHandler
    public void handle(DeleteQuizAttemptCommand command) {
        isQuizAttemptActive();

        QuizDeletedEvent event = new QuizDeletedEvent(command.id());

        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(QuizDeletedEvent event) {
        this.isActive = false;
    }

    private void isQuizAttemptActive() {
        if(!this.isActive) {
            throw new CommandExecutionException(
                    "error.quiz_attempt.deleted",
                    null,
                    quizId
            );
        }
    }
}