CREATE TYPE user_status AS ENUM ('stage1', 'stage2', 'stage3', 'stage4', 'ended', 'pending');
CREATE TYPE course_status AS ENUM ('ongoing', 'ended', 'template');
CREATE TYPE lesson_status AS ENUM ('active', 'not_active');
CREATE TYPE media_type AS ENUM ('photo', 'video', 'document', 'audio');
CREATE TYPE homework_done_status AS ENUM ('pending', 'uploaded');

CREATE TABLE users (
                       id INT PRIMARY KEY,
                       username TEXT NOT NULL,
                       email TEXT NOT NULL,
                       lastname TEXT NOT NULL,
                       middlename TEXT,
                       firstname TEXT NOT NULL,
                       password TEXT NOT NULL,
                       status user_status NOT NULL,
                       createDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                       updateDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE roles (
    roleName TEXT PRIMARY KEY
);

CREATE TABLE user_roles (
                            user_id INT,
                            role_name TEXT NOT NULL,
                            PRIMARY KEY (user_id, role_name),
                            FOREIGN KEY (user_id) REFERENCES Users(id),
                            FOREIGN KEY (role_name) REFERENCES roles(roleName)
);

CREATE TABLE courses (
                         id INT PRIMARY KEY,
                         course_name TEXT NOT NULL,
                         teacher_id INT,
                         status course_status NOT NULL,
                         create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                         update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                         FOREIGN KEY (teacher_id) REFERENCES users(id)
);

CREATE TABLE student_courses (
                              student_id INT NOT NULL,
                              course_id INT NOT NULL,
                              PRIMARY KEY (student_id, course_id),
                              FOREIGN KEY (student_id) REFERENCES users(id),
                              FOREIGN KEY (course_id) REFERENCES courses(id)
);

CREATE TABLE lessons (
                         id INT PRIMARY KEY,
                         course_id INT NOT NULL,
                         lessonName TEXT NOT NULL,
                         content TEXT,
                         status lesson_status NOT NULL,
                         createDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                         updateDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                         FOREIGN KEY (course_id) REFERENCES Courses(id)
);

CREATE TABLE homework_task (
                               id INT PRIMARY KEY,
                               lesson_id INT NOT NULL,
                               title TEXT NOT NULL,
                               description TEXT NOT NULL,
                               deadlineDate TIMESTAMP,
                               createDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                               updateDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                               FOREIGN KEY (lesson_id) REFERENCES Lessons(id)
);

CREATE TABLE homework_done (
                               id INT PRIMARY KEY,
                               task_id INT NOT NULL,
                               student_id INT NOT NULL,
                               submissionDate TIMESTAMP,
                               grade INT,
                               unique (task_id, student_id),
                               status homework_done_status NOT NULL,
                               FOREIGN KEY (task_id) REFERENCES homework_task(id),
                               FOREIGN KEY (student_id) REFERENCES Users(id)
);

CREATE TABLE teacher_courses (
                                 teacher_id INT NOT NULL,
                                 course_id INT NOT NULL,
                                 PRIMARY KEY (teacher_id, course_id),
                                 FOREIGN KEY (teacher_id) REFERENCES users(id),
                                 FOREIGN KEY (course_id) REFERENCES courses(id)
);

CREATE TABLE author_courses(
                               author_id INT NOT NULL,
                               course_id INT NOT NULL,
                               PRIMARY KEY (author_id, course_id),
                               FOREIGN KEY (author_id) REFERENCES users(id),
                               FOREIGN KEY (course_id) REFERENCES courses(id)
);

CREATE TABLE media_lesson (
                              id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
                              name TEXT NOT NULL,
                              size BIGINT NOT NULL,
                              type media_type NOT NULL,
                              lesson_id INT NOT NULL,
                              FOREIGN KEY (lesson_id) REFERENCES lessons(id)
);

CREATE TABLE media_homework_task (
                                     id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
                                     name TEXT NOT NULL,
                                     size BIGINT NOT NULL,
                                     type media_type NOT NULL,
                                     task_id INT NOT NULL,
                                     FOREIGN KEY (task_id) REFERENCES homework_task(id)
);

CREATE TABLE media_homework_done (
                                     id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
                                     name TEXT NOT NULL,
                                     size BIGINT NOT NULL,
                                     type media_type NOT NULL,
                                     homework_done_id INT NOT NULL,
                                     FOREIGN KEY (homework_done_id) REFERENCES homework_done(id)
);