-- Создание пользовательских типов
CREATE TYPE user_status AS ENUM ('stage1', 'stage2', 'stage3', 'stage4', 'ended', 'pending');
CREATE TYPE course_status AS ENUM ('ongoing', 'ended', 'template');
CREATE TYPE lesson_status AS ENUM ('active', 'not_active');
CREATE TYPE media_type AS ENUM ('photo', 'video', 'document', 'audio');
CREATE TYPE homework_done_status AS ENUM ('pending', 'uploaded');
CREATE TYPE ERole AS ENUM ('admin', 'user', 'author', 'student', 'moderator');

-- Создание таблицы пользователей
CREATE TABLE users (
                       id bigint PRIMARY KEY,
                       username TEXT NOT NULL,
                       email TEXT NOT NULL,
                       lastname TEXT NOT NULL,
                       middlename TEXT,
                       firstname TEXT NOT NULL,
                       password TEXT NOT NULL,
                       status user_status NOT NULL,
                       create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                       update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

-- Создание таблицы ролей
CREATE TABLE roles (
    id bigint PRIMARY KEY,
    name ERole
);

-- Создание таблицы связей между пользователями и ролями
CREATE TABLE user_roles (
                            user_id bigint,
                            role_id bigint NOT NULL,
                            PRIMARY KEY (user_id, role_id),
                            FOREIGN KEY (user_id) REFERENCES users(id),
                            FOREIGN KEY (role_id) REFERENCES roles(id)
);

-- Создание таблицы курсов
CREATE TABLE courses (
                         id bigint PRIMARY KEY,
                         course_name TEXT NOT NULL,
                         teacher_id bigint,  -- Заменен тип данных INT на bigint
                         status course_status NOT NULL,
                         create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                         update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                         FOREIGN KEY (teacher_id) REFERENCES users(id)
);

-- Создание таблицы связей между студентами и курсами
CREATE TABLE student_courses (
                                 student_id bigint NOT NULL,
                                 course_id bigint NOT NULL,
                                 PRIMARY KEY (student_id, course_id),
                                 FOREIGN KEY (student_id) REFERENCES users(id),
                                 FOREIGN KEY (course_id) REFERENCES courses(id)
);

-- Создание таблицы уроков
CREATE TABLE lessons (
                         id bigint PRIMARY KEY,
                         course_id bigint NOT NULL,
                         lesson_name TEXT NOT NULL,
                         content TEXT,
                         status lesson_status NOT NULL,
                         create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                         update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                         FOREIGN KEY (course_id) REFERENCES courses(id)
);

-- Создание таблицы домашних заданий
CREATE TABLE homework_task (
                               id bigint PRIMARY KEY,
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
                               id bigint PRIMARY KEY,
                               task_id bigint NOT NULL,
                               student_id bigint NOT NULL,
                               submission_date TIMESTAMP,
                               grade INT,
                               unique (task_id, student_id),
                               status homework_done_status NOT NULL,
                               FOREIGN KEY (task_id) REFERENCES homework_task(id),
                               FOREIGN KEY (student_id) REFERENCES users(id)
);

-- Создание таблицы связей между преподавателями и курсами
CREATE TABLE teacher_courses (
                                 teacher_id bigint NOT NULL,
                                 course_id bigint NOT NULL,
                                 PRIMARY KEY (teacher_id, course_id),
                                 FOREIGN KEY (teacher_id) REFERENCES users(id),
                                 FOREIGN KEY (course_id) REFERENCES courses(id)
);

-- Создание таблицы связей между авторами и курсами
CREATE TABLE author_courses (
                                author_id bigint NOT NULL,
                                course_id bigint NOT NULL,
                                PRIMARY KEY (author_id, course_id),
                                FOREIGN KEY (author_id) REFERENCES users(id),
                                FOREIGN KEY (course_id) REFERENCES courses(id)
);

-- Создание таблицы медиа для уроков
CREATE TABLE media_lesson (
                              id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
                              name TEXT NOT NULL,
                              size BIGINT NOT NULL,
                              type media_type NOT NULL,
                              lesson_id bigint NOT NULL,  -- Заменен тип данных INT на bigint
                              FOREIGN KEY (lesson_id) REFERENCES lessons(id)
);

-- Создание таблицы медиа для домашних заданий
CREATE TABLE media_homework_task (
                                     id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
                                     name TEXT NOT NULL,
                                     size BIGINT NOT NULL,
                                     type media_type NOT NULL,
                                     task_id bigint NOT NULL,  -- Заменен тип данных INT на bigint
                                     FOREIGN KEY (task_id) REFERENCES homework_task(id)
);

-- Создание таблицы медиа для завершенных домашних заданий
CREATE TABLE media_homework_done (
                                     id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
                                     name TEXT NOT NULL,
                                     size BIGINT NOT NULL,
                                     type media_type NOT NULL,
                                     homework_done_id bigint NOT NULL,  -- Заменен тип данных INT на bigint
                                     FOREIGN KEY (homework_done_id) REFERENCES homework_done(id)
);

CREATE SEQUENCE refresh_token_seq START 1;

-- Создание таблицы для токенов обновления
CREATE TABLE refresh_token (
                               id bigint PRIMARY KEY DEFAULT nextval('refresh_token_seq'),
                               user_id bigint,
                               token TEXT NOT NULL,
                               expiry_date TIMESTAMP NOT NULL,
                               FOREIGN KEY (user_id) REFERENCES users(id)
);