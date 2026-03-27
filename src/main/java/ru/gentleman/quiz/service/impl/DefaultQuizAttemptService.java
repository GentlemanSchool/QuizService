package ru.gentleman.quiz.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gentleman.common.util.ExceptionUtils;
import ru.gentleman.quiz.cache.CacheClear;
import ru.gentleman.quiz.dto.QuizAttemptDto;
import ru.gentleman.quiz.entity.QuizAttempt;
import ru.gentleman.quiz.mapper.QuizAttemptMapper;
import ru.gentleman.quiz.repository.QuizAttemptRepository;
import ru.gentleman.quiz.service.QuizAttemptService;
import ru.gentleman.quiz.service.QuizService;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DefaultQuizAttemptService implements QuizAttemptService {

    private final QuizAttemptRepository quizAttemptRepository;

    private final QuizAttemptMapper quizAttemptMapper;

    private final QuizService quizService;

    private final CacheClear cacheClear;

    @Override
    @Cacheable(value = "quizAttempt", key = "#id")
    public QuizAttemptDto get(UUID id) {
        log.info("get {}", id);

        QuizAttempt quizAttempt =
                this.quizAttemptRepository.findById(id).orElse(null);

        return this.quizAttemptMapper.toDto(quizAttempt);
    }

    @Override
    @Cacheable(value = "allQuizAttemptsByUserId", key = "#userId")
    public List<QuizAttemptDto> getAllByUserId(UUID userId) {
        log.info("getAllByUserId {}", userId);

        return this.quizAttemptMapper.toDto(
                this.quizAttemptRepository.findAllByUserIdAndIsActive(userId, true)
        );
    }

    @Override
    @Transactional
    public QuizAttemptDto create(QuizAttemptDto dto) {
        log.info("create {}", dto);

        this.quizService.get(dto.quizId());
        QuizAttempt quizAttempt = this.quizAttemptMapper.toEntity(dto);
        quizAttempt.setIsActive(true);

        QuizAttempt createdQuizAttempt = this.quizAttemptRepository.save(quizAttempt);

        this.cacheClear.clearAllQuizAttemptsByUserId(quizAttempt.getUserId());

        return this.quizAttemptMapper.toDto(createdQuizAttempt);
    }

    @Override
    @Transactional
    @CacheEvict(value = "quizAttempt", key = "#id")
    public void finish(UUID id, int finalScore, Instant completedAt) {
        log.info("finish {} {}", id, finalScore);

        QuizAttempt quizAttempt = this.quizAttemptRepository.findById(id)
                .orElseThrow(() -> ExceptionUtils.notFound("error.quiz_attempt.not_found_id",id));

        quizAttempt.setCompletedAt(completedAt);
        quizAttempt.setFinalScore(finalScore);
    }

    @Override
    public boolean existsById(UUID id) {
        log.info("existsById {}", id);

        return this.quizAttemptRepository.existsById(id);
    }

    @Override
    @Transactional
    @CacheEvict(value = "quizAttempt", key = "#id")
    public void delete(UUID id) {
        log.info("delete {}", id);

        QuizAttempt quizAttempt = this.quizAttemptRepository.findById(id)
                .orElseThrow(() -> ExceptionUtils.notFound("error.quiz_attempt.not_found_id",id));

        quizAttempt.setIsActive(false);

        this.cacheClear.clearAllQuizAttemptsByUserId(quizAttempt.getUserId());
    }
}
