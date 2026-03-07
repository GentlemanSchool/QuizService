package ru.gentleman.quiz;

import org.springframework.boot.SpringApplication;

public class TestGentlemanQuizServiceApplication {

    public static void main(String[] args) {
        SpringApplication.from(GentlemanQuizServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
