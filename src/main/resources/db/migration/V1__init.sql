CREATE TYPE user_status AS ENUM ('stage1', 'stage2', 'stage3', 'stage4', 'ended', 'pending');
CREATE TYPE course_status AS ENUM ('ongoing', 'ended', 'template');
CREATE TYPE lesson_status AS ENUM ('active', 'not_active');
CREATE TYPE media_type AS ENUM ('photo', 'video', 'document', 'audio');
CREATE TYPE homework_done_status AS ENUM ('pending', 'uploaded');

CREATE TABLE users (
                       id INT PRIMARY KEY,
                       username TEXT,
                       email TEXT,
                       lastname TEXT,
                       middlename TEXT,
                       firstname TEXT,
                       password TEXT,
                       status user_status,
                       create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE roles (
                    role_name TEXT PRIMARY KEY
);

CREATE TABLE user_roles (
                            user_id INT,
                            role_name TEXT,
                            PRIMARY KEY (user_id, role_name),
                            FOREIGN KEY (user_id) REFERENCES Users(id),
                            FOREIGN KEY (role_name) REFERENCES roles(role_name)
);

CREATE TABLE courses (
                         id INT PRIMARY KEY,
                         course_name TEXT,
                         teacher_id INT,
                         status course_status,
                         create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         FOREIGN KEY (teacher_id) REFERENCES users(id)
);

CREATE TABLE user_courses (
                              user_id INT,
                              course_id INT,
                              PRIMARY KEY (user_id, course_id),
                              FOREIGN KEY (user_id) REFERENCES users(id),
                              FOREIGN KEY (course_id) REFERENCES courses(id)
);

CREATE TABLE lessons (
                         id INT PRIMARY KEY,
                         course_id INT,
                         lesson_name TEXT,
                         content TEXT,
                         status lesson_status,
                         create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         FOREIGN KEY (course_id) REFERENCES Courses(id)
);

CREATE TABLE homework_task (
                             id INT PRIMARY KEY,
                             lesson_id INT,
                             title TEXT,
                             description TEXT,
                             deadline_date timestamp,
                             create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             FOREIGN KEY (lesson_id) REFERENCES Lessons(id)
);

CREATE TABLE homework_done (
                                       id INT PRIMARY KEY,
                                       task_id INT,
                                       student_id INT,
                                       submission_date TIMESTAMP,
                                       grade INT,
                                       unique (task_id, student_id),
                                       status homework_done_status,
                                       FOREIGN KEY (task_id) REFERENCES homework_task(id),
                                       FOREIGN KEY (student_id) REFERENCES Users(id)
);

CREATE TABLE media_lesson (
                            id uuid PRIMARY KEY,
                            name TEXT,
                            size bigint,
                            type media_type,
                            lesson_id INT,
                            FOREIGN KEY (lesson_id) REFERENCES lessons(id)
);

CREATE TABLE media_homework_task (
                                     id uuid PRIMARY KEY,
                                     name TEXT,
                                     size bigint,
                                     type media_type,
                                     task_id INT,
                                     FOREIGN KEY (task_id) REFERENCES homework_task(id)
);

CREATE TABLE media_homework_done (
                              id uuid PRIMARY KEY,
                              name TEXT,
                              size bigint,
                              type media_type,
                              homework_done_id INT,
                              FOREIGN KEY (homework_done_id) REFERENCES homework_done(id)
);
