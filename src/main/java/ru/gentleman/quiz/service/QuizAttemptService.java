package ru.gentleman.quiz.service;

import ru.gentleman.quiz.dto.QuizAttemptDto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface QuizAttemptService  {

    QuizAttemptDto get(UUID id);

    List<QuizAttemptDto> getAllByUserId(UUID userId);

    QuizAttemptDto create(QuizAttemptDto dto);

    void finish(UUID id, int finalScore, Instant completedAt);

    boolean existsById(UUID id);

    void delete(UUID id);
}
