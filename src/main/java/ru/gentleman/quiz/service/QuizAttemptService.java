package ru.gentleman.quiz.service;

import ru.gentleman.quiz.dto.QuizAttemptDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface QuizAttemptService  {

    QuizAttemptDto get(UUID id);

    List<QuizAttemptDto> getAllByUserId(UUID userId);

    void create(QuizAttemptDto dto);

    void finish(UUID id, int finalScore, LocalDateTime completedAt);

    boolean existsById(UUID id);

    void delete(UUID id);
}
