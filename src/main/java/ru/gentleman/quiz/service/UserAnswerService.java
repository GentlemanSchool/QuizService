package ru.gentleman.quiz.service;

import ru.gentleman.quiz.dto.UserAnswerDto;

import java.util.List;
import java.util.UUID;

public interface UserAnswerService {

    UserAnswerDto get(UUID id);

    List<UserAnswerDto> getAllByUserId(UUID userId);

    void create(UserAnswerDto userAnswerDto);
}
