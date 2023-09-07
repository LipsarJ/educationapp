ALTER TABLE homework_done
    ADD COLUMN teacher_id BIGINT,
    ADD CONSTRAINT fk_homework_done_teacher FOREIGN KEY (teacher_id) REFERENCES users(id);