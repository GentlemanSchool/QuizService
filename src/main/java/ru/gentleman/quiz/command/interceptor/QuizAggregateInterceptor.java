package ru.gentleman.quiz.command.interceptor;

import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.springframework.stereotype.Component;
import ru.gentleman.common.util.ExceptionUtils;
import ru.gentleman.quiz.command.*;
import ru.gentleman.quiz.service.QuizService;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.BiFunction;

@Component
@RequiredArgsConstructor
public class QuizAggregateInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {

    private final QuizService quizService;

    @Nonnull
    @Override
    public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(@Nonnull List<? extends CommandMessage<?>> messages) {
        return (index, command) -> {
            if(UpdateQuizCommand.class.equals(command.getPayloadType())) {
                UpdateQuizCommand updateQuizCommand = (UpdateQuizCommand) command.getPayload();

                if(!this.quizService.existsById(updateQuizCommand.id())){
                    ExceptionUtils.notFound("error.quiz.not_found", updateQuizCommand.id());
                }
            } else if (DeleteQuizCommand.class.equals(command.getPayloadType())) {
                DeleteQuizCommand deleteQuizCommand = (DeleteQuizCommand) command.getPayload();

                if(!this.quizService.existsById(deleteQuizCommand.id())){
                    ExceptionUtils.notFound("error.quiz.not_found", deleteQuizCommand.id());
                }
            }  else if (CreateQuestionCommand.class.equals(command.getPayloadType())) {
                CreateQuestionCommand createQuestionCommand = (CreateQuestionCommand) command.getPayload();

                if(!this.quizService.existsById(createQuestionCommand.quizId())){
                    ExceptionUtils.notFound("error.quiz.not_found", createQuestionCommand.quizId());
                }
            } else if (UpdateQuestionCommand.class.equals(command.getPayloadType())) {
                UpdateQuestionCommand updateQuestionCommand = (UpdateQuestionCommand) command.getPayload();

                if(!this.quizService.existsById(updateQuestionCommand.quizId())){
                    ExceptionUtils.notFound("error.quiz.not_found", updateQuestionCommand.id());
                }
            }

            return command;
        };
    }
}
