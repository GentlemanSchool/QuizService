package ru.gentleman.quiz.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(schema = "quiz", name = "questions")
@AllArgsConstructor
@NoArgsConstructor
public class Question {

    @Id
    @UuidGenerator
    @GeneratedValue
    private UUID id;

    private String title;

    private String correctAnswer;

    private String explanation;

    @ManyToOne
    @JoinColumn(name = "quiz_id", referencedColumnName = "id")
    private Quiz quiz;
}
