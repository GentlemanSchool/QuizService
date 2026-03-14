package ru.gentleman.quiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.gentleman.quiz.entity.QuizAttempt;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, UUID> {

    Optional<QuizAttempt> findByIdAndIsActive(UUID id, Boolean isActive);

    List<QuizAttempt> findAllByUserIdAndIsActive(UUID userId, Boolean isActive);
}
