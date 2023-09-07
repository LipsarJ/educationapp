ALTER TABLE homework_done
ADD COLUMN student_description TEXT;

ALTER TABLE homework_done
ADD COLUMN teacher_feedback TEXT;

ALTER TABLE homework_done
ADD COLUMN teacher_id BIGINT;