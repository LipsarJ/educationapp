-- Создание таблицы пользователей
CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       username TEXT NOT NULL,
                       email TEXT NOT NULL,
                       lastname TEXT NOT NULL,
                       middlename TEXT,
                       firstname TEXT NOT NULL,
                       password TEXT NOT NULL,
                       status TEXT NOT NULL,
                       create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                       update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

-- Создание таблицы ролей
CREATE TABLE roles (
                       id bigint PRIMARY KEY,
                       role_name TEXT
);
INSERT INTO roles (id, role_name) VALUES (1, 'ADMIN');
INSERT INTO roles (id, role_name) VALUES (2, 'AUTHOR');
INSERT INTO roles (id, role_name) VALUES (3, 'STUDENT');
INSERT INTO roles (id, role_name) VALUES (4, 'TEACHER');
INSERT INTO roles (id, role_name) VALUES (5, 'MODERATOR');

-- Создание таблицы связей между пользователями и ролями
CREATE TABLE user_roles (
                            user_id BIGSERIAL,
                            role_id BIGSERIAL NOT NULL,
                            PRIMARY KEY (user_id, role_id),
                            FOREIGN KEY (user_id) REFERENCES users(id),
                            FOREIGN KEY (role_id) REFERENCES roles(id)
);

-- Создание таблицы курсов
CREATE TABLE courses (
                         id BIGSERIAL PRIMARY KEY,
                         course_name TEXT NOT NULL,
                         teacher_id bigint,  -- Заменен тип данных INT на bigint
                         course_status TEXT NOT NULL,
                         create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                         update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                         FOREIGN KEY (teacher_id) REFERENCES users(id)
);

-- Создание таблицы связей между студентами и курсами
CREATE TABLE student_courses (
                                 student_id BIGSERIAL NOT NULL,
                                 course_id BIGSERIAL NOT NULL,
                                 PRIMARY KEY (student_id, course_id),
                                 FOREIGN KEY (student_id) REFERENCES users(id),
                                 FOREIGN KEY (course_id) REFERENCES courses(id)
);

-- Создание таблицы уроков
CREATE TABLE lessons (
                         id BIGSERIAL PRIMARY KEY,
                         course_id bigint NOT NULL,
                         lesson_name TEXT NOT NULL,
                         content TEXT,
                         lesson_status TEXT NOT NULL,
                         create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                         update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                         FOREIGN KEY (course_id) REFERENCES courses(id)
);

-- Создание таблицы домашних заданий
CREATE TABLE homework_task (
                               id BIGSERIAL PRIMARY KEY,
                               lesson_id bigint NOT NULL,
                               title TEXT NOT NULL,
                               description TEXT NOT NULL,
                               deadline_date TIMESTAMP,
                               create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                               update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                               FOREIGN KEY (lesson_id) REFERENCES lessons(id)
);

-- Создание таблицы завершенных домашних заданий
CREATE TABLE homework_done (
                               id BIGSERIAL PRIMARY KEY,
                               task_id bigint NOT NULL,
                               student_id bigint NOT NULL,
                               submission_date TIMESTAMP,
                               grade INT,
                               unique (task_id, student_id),
                               status TEXT NOT NULL,
                               FOREIGN KEY (task_id) REFERENCES homework_task(id),
                               FOREIGN KEY (student_id) REFERENCES users(id)
);

-- Создание таблицы связей между преподавателями и курсами
CREATE TABLE teacher_courses (
                                 teacher_id BIGSERIAL NOT NULL,
                                 course_id BIGSERIAL NOT NULL,
                                 PRIMARY KEY (teacher_id, course_id),
                                 FOREIGN KEY (teacher_id) REFERENCES users(id),
                                 FOREIGN KEY (course_id) REFERENCES courses(id)
);

-- Создание таблицы связей между авторами и курсами
CREATE TABLE author_courses (
                                author_id BIGSERIAL NOT NULL,
                                course_id BIGSERIAL NOT NULL,
                                PRIMARY KEY (author_id, course_id),
                                FOREIGN KEY (author_id) REFERENCES users(id),
                                FOREIGN KEY (course_id) REFERENCES courses(id)
);

-- Создание таблицы медиа для уроков
CREATE TABLE media_lesson (
                              id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
                              name TEXT NOT NULL,
                              size BIGINT NOT NULL,
                              media_type TEXT NOT NULL,
                              lesson_id bigint NOT NULL,  -- Заменен тип данных INT на bigint
                              FOREIGN KEY (lesson_id) REFERENCES lessons(id)
);

-- Создание таблицы медиа для домашних заданий
CREATE TABLE media_homework_task (
                                     id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
                                     name TEXT NOT NULL,
                                     size BIGINT NOT NULL,
                                     media_type TEXT NOT NULL,
                                     task_id bigint NOT NULL,  -- Заменен тип данных INT на bigint
                                     FOREIGN KEY (task_id) REFERENCES homework_task(id)
);

-- Создание таблицы медиа для завершенных домашних заданий
CREATE TABLE media_homework_done (
                                     id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
                                     name TEXT NOT NULL,
                                     size BIGINT NOT NULL,
                                     media_type TEXT NOT NULL,
                                     homework_done_id bigint NOT NULL,  -- Заменен тип данных INT на bigint
                                     FOREIGN KEY (homework_done_id) REFERENCES homework_done(id)
);

CREATE SEQUENCE refresh_token_seq START 1;

-- Создание таблицы для токенов обновления
CREATE TABLE refresh_token (
                               id BIGSERIAL PRIMARY KEY,
                               user_id BIGSERIAL,
                               token TEXT NOT NULL,
                               expiry_date TIMESTAMP NOT NULL,
                               FOREIGN KEY (user_id) REFERENCES users(id)
);
