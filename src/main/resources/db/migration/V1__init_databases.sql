CREATE SCHEMA IF NOT EXISTS quiz;

CREATE TABLE IF NOT EXISTS quiz.quizzes(
    id UUID PRIMARY KEY ,
    title VARCHAR(100) NOT NULL ,
    description VARCHAR(1000) NOT NULL ,
    lesson_id UUID ,
    is_active BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS quiz.questions(
    id UUID PRIMARY KEY ,
    quiz_id UUID REFERENCES quiz.quizzes(id) ON DELETE SET NULL ,
    title VARCHAR(100) NOT NULL ,
    description TEXT NOT NULL ,
    correct_answer VARCHAR(100) NOT NULL ,
    explanation TEXT NOT NULL ,
    score INT NOT NULL ,
    is_active BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS quiz.quiz_attempts(
    id UUID PRIMARY KEY ,
    user_id UUID NOT NULL ,
    quiz_id UUID REFERENCES quiz.quizzes(id) ON DELETE SET NULL,
    final_score INT,
    created_at TIMESTAMP NOT NULL ,
    completed_at TIMESTAMP ,
    is_active BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS quiz.user_answers(
    id UUID PRIMARY KEY ,
    question_id UUID REFERENCES quiz.questions(id) ON DELETE SET NULL ,
    quiz_attempt_id UUID REFERENCES quiz.quiz_attempts(id) ON DELETE SET NULL,
    user_id UUID NOT NULL ,
    answer VARCHAR(100) NOT NULL ,
    is_correct BOOLEAN NOT NULL ,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS domain_event_entry (
    global_index BIGSERIAL PRIMARY KEY ,
    event_identifier VARCHAR(255) NOT NULL UNIQUE ,
    meta_data BYTEA NOT NULL ,
    payload BYTEA NOT NULL ,
    payload_revision VARCHAR(255) ,
    payload_type VARCHAR(255) NOT NULL ,
    time_stamp VARCHAR(255) NOT NULL ,
    aggregate_identifier VARCHAR(255) NOT NULL ,
    sequence_number BIGINT NOT NULL ,
    type VARCHAR(255) NOT NULL ,
    UNIQUE (aggregate_identifier, sequence_number, type)
);

CREATE TABLE IF NOT EXISTS snapshot_event_entry (
    event_identifier VARCHAR(255) NOT NULL UNIQUE,
    meta_data BYTEA NOT NULL,
    payload BYTEA NOT NULL,
    payload_revision VARCHAR(255),
    payload_type VARCHAR(255) NOT NULL,
    time_stamp VARCHAR(255) NOT NULL,
    aggregate_identifier VARCHAR(255) NOT NULL,
    sequence_number BIGINT NOT NULL,
    type VARCHAR(255) NOT NULL,
    PRIMARY KEY (aggregate_identifier, sequence_number, type)
);

CREATE TABLE IF NOT EXISTS token_entry (
    processor_name VARCHAR(255) NOT NULL,
    segment INTEGER NOT NULL,
    owner VARCHAR(255),
    timestamp VARCHAR(255) NOT NULL,
    token BYTEA,
    token_type VARCHAR(255),
    PRIMARY KEY (processor_name, segment)
);

CREATE TABLE IF NOT EXISTS saga_entry (
    saga_id VARCHAR(255) PRIMARY KEY,
    revision VARCHAR(255),
    saga_type VARCHAR(255),
    serialized_saga BYTEA
);

CREATE TABLE IF NOT EXISTS association_value_entry (
    id BIGINT PRIMARY KEY,
    association_key VARCHAR(255) NOT NULL,
    association_value VARCHAR(255),
    saga_id VARCHAR(255) NOT NULL,
    saga_type VARCHAR(255)
);

CREATE SEQUENCE IF NOT EXISTS association_value_entry_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE IF NOT EXISTS dead_letter_entry (
    dead_letter_id VARCHAR(255) PRIMARY KEY,
    cause_message VARCHAR(1023),
    cause_type VARCHAR(255),
    diagnostics BYTEA,
    enqueued_at TIMESTAMPTZ NOT NULL,
    last_touched TIMESTAMPTZ,
    aggregate_identifier VARCHAR(255),
    event_identifier VARCHAR(255) NOT NULL,
    message_type VARCHAR(255) NOT NULL,
    meta_data BYTEA,
    payload BYTEA NOT NULL,
    payload_revision VARCHAR(255),
    payload_type VARCHAR(255) NOT NULL,
    sequence_number BIGINT,
    time_stamp VARCHAR(255) NOT NULL,
    token BYTEA,
    token_type VARCHAR(255),
    type VARCHAR(255),
    processing_group VARCHAR(255) NOT NULL,
    processing_started TIMESTAMPTZ,
    sequence_identifier VARCHAR(255) NOT NULL,
    sequence_index BIGINT NOT NULL,
    CONSTRAINT uk_dead_letter_unique UNIQUE (processing_group, sequence_identifier, sequence_index)
);

CREATE INDEX IF NOT EXISTS idx_dead_letter_processing_group    ON dead_letter_entry (processing_group);
CREATE INDEX IF NOT EXISTS idx_dead_letter_sequence_identifier ON dead_letter_entry (sequence_identifier);