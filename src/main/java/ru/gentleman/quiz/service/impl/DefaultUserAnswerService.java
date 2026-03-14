package ru.gentleman.quiz.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gentleman.quiz.dto.UserAnswerDto;
import ru.gentleman.quiz.entity.UserAnswer;
import ru.gentleman.quiz.mapper.UserAnswerMapper;
import ru.gentleman.quiz.repository.UserAnswerRepository;
import ru.gentleman.quiz.service.UserAnswerService;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class DefaultUserAnswerService implements UserAnswerService  {

    private final UserAnswerRepository userAnswerRepository;

    private final UserAnswerMapper userAnswerMapper;

    @Override
    @Cacheable(value = "userAnswer", key = "#id")
    public UserAnswerDto get(UUID id) {
        log.info("get {}", id);

        UserAnswer userAnswer = this.userAnswerRepository.findById(id)
                .orElse(null);

        return this.userAnswerMapper.toDto(userAnswer);
    }

    @Override
    @Cacheable(value = "allUserAnswersByUserId", key = "#userId")
    public List<UserAnswerDto> getAllByUserId(UUID userId) {
        log.info("getAllByUserId {}", userId);

        return this.userAnswerMapper.toDto(
                this.userAnswerRepository.findAllByUserId(userId)
        );
    }

    @Override
    @CacheEvict(value = "allUserAnswersByUserId", key = "#dto.userId()")
    public void create(UserAnswerDto dto) {
        log.info("create {}", dto);

        this.userAnswerRepository.save(
                this.userAnswerMapper.toEntity(dto)
        );
    }
}
