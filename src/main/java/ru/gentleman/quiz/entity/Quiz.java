package ru.gentleman.quiz.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(schema = "quiz", name = "quizzes")
public class Quiz {

    @Id
    @UuidGenerator
    @GeneratedValue
    private UUID id;

    private String title;

    private UUID lessonId;

    @OneToMany(mappedBy = "quiz")
    private List<Question> questions;
}
