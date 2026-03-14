package ru.gentleman.quiz.command.interceptor;

import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.springframework.stereotype.Component;
import ru.gentleman.common.util.ExceptionUtils;
import ru.gentleman.quiz.command.CreateQuizAttemptCommand;
import ru.gentleman.quiz.command.CreateUserAnswerCommand;
import ru.gentleman.quiz.command.DeleteQuizAttemptCommand;
import ru.gentleman.quiz.command.FinishQuizAttemptCommand;
import ru.gentleman.quiz.service.QuizAttemptService;
import ru.gentleman.quiz.service.QuizService;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.BiFunction;

@Component
@RequiredArgsConstructor
public class QuizAttemptAggregateInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {

    private final QuizAttemptService quizAttemptService;

    private final QuizService quizService;

    @Nonnull
    @Override
    public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(@Nonnull List<? extends CommandMessage<?>> messages) {
        return (index, command) -> {
            if(CreateQuizAttemptCommand.class.equals(command.getPayloadType())) {
                CreateQuizAttemptCommand createQuizAttemptCommand = (CreateQuizAttemptCommand) command.getPayload();

                if(!this.quizService.existsById(createQuizAttemptCommand.quizId())){
                    ExceptionUtils.notFound("error.quiz.not_found", createQuizAttemptCommand.id());
                }
            } else if(DeleteQuizAttemptCommand.class.equals(command.getPayloadType())) {
                DeleteQuizAttemptCommand deleteQuizAttemptCommand = (DeleteQuizAttemptCommand) command.getPayload();

                if(!this.quizAttemptService.existsById(deleteQuizAttemptCommand.id())){
                    ExceptionUtils.notFound("error.quiz_attempt.not_found", deleteQuizAttemptCommand.id());
                }
            } else if (CreateUserAnswerCommand.class.equals(command.getPayloadType())) {
                CreateUserAnswerCommand createUserAnswerCommand = (CreateUserAnswerCommand) command.getPayload();

                if(!this.quizAttemptService.existsById(createUserAnswerCommand.quizAttemptId())){
                    ExceptionUtils.notFound("error.quiz_attempt.not_found", createUserAnswerCommand.quizAttemptId());
                }
            }  else if (FinishQuizAttemptCommand.class.equals(command.getPayloadType())) {
                FinishQuizAttemptCommand finishQuizAttemptCommand = (FinishQuizAttemptCommand) command.getPayload();

                if(!this.quizAttemptService.existsById(finishQuizAttemptCommand.id())){
                    ExceptionUtils.notFound("error.quiz_attempt.not_found", finishQuizAttemptCommand.id());
                }
            }

            return command;
        };
    }
}
