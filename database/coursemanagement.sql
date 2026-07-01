-- TẠO ENUMS
CREATE TYPE user_role AS ENUM ('ADMIN', 'TEACHER', 'STUDENT');
CREATE TYPE course_status AS ENUM ('DRAFT', 'PUBLISHED', 'ARCHIVED');
CREATE TYPE enrollment_status AS ENUM ('ENROLLED', 'COMPLETED', 'DROPPED');

-- 1. USERS
CREATE TABLE users (
    user_id       SERIAL PRIMARY KEY,
    username      VARCHAR(50)  UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    email         VARCHAR(100) UNIQUE NOT NULL,
    full_name     VARCHAR(100) NOT NULL,
    role          user_role    NOT NULL DEFAULT 'STUDENT',
    is_active     BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 2. COURSES
CREATE TABLE courses (
    course_id      SERIAL PRIMARY KEY,
    title          VARCHAR(255)   NOT NULL,
    description    TEXT,
    teacher_id     INT            NOT NULL REFERENCES users(user_id),
    price          DECIMAL(10,2)  NOT NULL DEFAULT 0.00,
    duration_hours INT,
    status         course_status  NOT NULL DEFAULT 'DRAFT',
    created_at     TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 3. LESSONS
CREATE TABLE lessons (
    lesson_id    SERIAL PRIMARY KEY,
    course_id    INT          NOT NULL REFERENCES courses(course_id) ON DELETE CASCADE,
    title        VARCHAR(255) NOT NULL,
    content_url  VARCHAR(500),
    text_content TEXT,
    order_index  INT          NOT NULL,
    is_published BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 4. ENROLLMENTS
CREATE TABLE enrollments (
    enrollment_id       SERIAL PRIMARY KEY,
    student_id          INT               NOT NULL REFERENCES users(user_id),
    course_id           INT               NOT NULL REFERENCES courses(course_id),
    enrollment_date     TIMESTAMP         NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status              enrollment_status NOT NULL DEFAULT 'ENROLLED',
    completion_date     TIMESTAMP,
    progress_percentage DECIMAL(5,2)      NOT NULL DEFAULT 0.00
        CHECK (progress_percentage >= 0 AND progress_percentage <= 100),
    UNIQUE (student_id, course_id)
);

-- 5. LESSON_PROGRESS
CREATE TABLE lesson_progress (
    progress_id      SERIAL PRIMARY KEY,
    enrollment_id    INT       NOT NULL REFERENCES enrollments(enrollment_id) ON DELETE CASCADE,
    lesson_id        INT       NOT NULL REFERENCES lessons(lesson_id),
    is_completed     BOOLEAN   NOT NULL DEFAULT FALSE,
    completed_at     TIMESTAMP,
    last_accessed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (enrollment_id, lesson_id)
);

-- 6. REVIEWS
CREATE TABLE reviews (
    review_id  SERIAL PRIMARY KEY,
    course_id  INT       NOT NULL REFERENCES courses(course_id) ON DELETE CASCADE,
    student_id INT       NOT NULL REFERENCES users(user_id),
    rating     INT       NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment    TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (course_id, student_id)
);

-- 7. NOTIFICATIONS
CREATE TABLE notifications (
    notification_id SERIAL PRIMARY KEY,
    user_id         INT          NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    message         TEXT         NOT NULL,
    type            VARCHAR(50),
    target_url      VARCHAR(500),
    is_read         BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- SEED DATA (password tất cả: Password@123)
INSERT INTO users (username, password_hash, email, full_name, role, is_active) VALUES
('admin',    '$2a$10$slYQmyNdgzSx.numOf5XleL8K/xt8VYbCMBsOvWGIFmxIcGVTHtKm', 'admin@system.com',    'System Admin',  'ADMIN',   true),
('teacher1', '$2a$10$slYQmyNdgzSx.numOf5XleL8K/xt8VYbCMBsOvWGIFmxIcGVTHtKm', 'teacher1@school.com', 'Nguyễn Văn An', 'TEACHER', true),
('teacher2', '$2a$10$slYQmyNdgzSx.numOf5XleL8K/xt8VYbCMBsOvWGIFmxIcGVTHtKm', 'teacher2@school.com', 'Trần Thị Bình', 'TEACHER', true),
('student1', '$2a$10$slYQmyNdgzSx.numOf5XleL8K/xt8VYbCMBsOvWGIFmxIcGVTHtKm', 'student1@mail.com',   'Lê Văn Cường',  'STUDENT', true),
('student2', '$2a$10$slYQmyNdgzSx.numOf5XleL8K/xt8VYbCMBsOvWGIFmxIcGVTHtKm', 'student2@mail.com',   'Phạm Thị Dung', 'STUDENT', true),
('student3', '$2a$10$slYQmyNdgzSx.numOf5XleL8K/xt8VYbCMBsOvWGIFmxIcGVTHtKm', 'student3@mail.com',   'Hoàng Văn Em',  'STUDENT', false);

INSERT INTO courses (title, description, teacher_id, price, duration_hours, status) VALUES
('Java Spring Boot cơ bản',   'Học Spring Boot từ đầu đến REST API hoàn chỉnh', 2, 499000, 30, 'PUBLISHED'),
('Java Spring Boot nâng cao', 'Security, JWT, Microservices với Spring Boot',    2, 799000, 45, 'PUBLISHED'),
('React.js cho người mới',    'Xây dựng UI hiện đại với React và TypeScript',    3, 599000, 25, 'PUBLISHED'),
('Thiết kế UI/UX cơ bản',    'Figma, nguyên tắc thiết kế, prototype',           3, 399000, 20, 'DRAFT'),
('Python Data Science',       'Pandas, NumPyyNumPyy, Matplotlib cho phân tích dữ liệu',2, 699000, 35, 'ARCHIVED');

INSERT INTO lessons (course_id, title, content_url, order_index, is_published) VALUES
(1, 'Giới thiệu Spring Boot',      'https://storage/lessons/1-1.mp4', 1, true),
(1, 'Cài đặt môi trường',          'https://storage/lessons/1-2.mp4', 2, true),
(1, 'Tạo project đầu tiên',        'https://storage/lessons/1-3.mp4', 3, true),
(1, 'REST Controller cơ bản',      'https://storage/lessons/1-4.mp4', 4, true),
(1, 'Kết nối database với JPA',    'https://storage/lessons/1-5.mp4', 5, false),
(2, 'Spring Security overview',    'https://storage/lessons/2-1.mp4', 1, true),
(2, 'JWT Authentication',          'https://storage/lessons/2-2.mp4', 2, true),
(2, 'Authorization và phân quyền', 'https://storage/lessons/2-3.mp4', 3, true),
(3, 'React là gì?',                'https://storage/lessons/3-1.mp4', 1, true),
(3, 'Components và Props',         'https://storage/lessons/3-2.mp4', 2, true),
(3, 'State và Hooks',              'https://storage/lessons/3-3.mp4', 3, true);

INSERT INTO enrollments (student_id, course_id, status, progress_percentage) VALUES
(4, 1, 'ENROLLED',   50.00),
(4, 2, 'ENROLLED',    0.00),
(5, 1, 'COMPLETED', 100.00),
(5, 3, 'ENROLLED',   33.33);

INSERT INTO lesson_progress (enrollment_id, lesson_id, is_completed, completed_at) VALUES
(1, 1, true,  NOW() - INTERVAL '3 days'),
(1, 2, true,  NOW() - INTERVAL '2 days'),
(1, 3, false, null),
(1, 4, false, null),
(3, 1, true,  NOW() - INTERVAL '10 days'),
(3, 2, true,  NOW() - INTERVAL '9 days'),
(3, 3, true,  NOW() - INTERVAL '8 days'),
(3, 4, true,  NOW() - INTERVAL '7 days');

INSERT INTO reviews (course_id, student_id, rating, comment) VALUES
(1, 5, 5, 'Khóa học rất hay, giảng viên giải thích dễ hiểu!'),
(1, 4, 4, 'Nội dung tốt nhưng cần thêm bài tập thực hành.'),
(3, 5, 5, 'React được giải thích rất rõ ràng, highly recommend!');

INSERT INTO notifications (user_id, message, type, is_read) VALUES
(4, 'Bạn đã đăng ký khóa học Java Spring Boot cơ bản thành công!', 'ENROLLMENT', true),
(4, 'Khóa học Java Spring Boot nâng cao có bài học mới!',           'NEW_LESSON',  false),
(5, 'Chúc mừng bạn đã hoàn thành khóa học Java Spring Boot!',      'COMPLETION',  false);


-- Thay HASH bằng hash vừa copy từ console IntelliJ
UPDATE users 
SET password_hash = '$2a$10$sjtzoEunNfv9cefE18bcluLYL4Do8R.WAF/vmoBUsoRVggNzYjK9e'
WHERE username IN ('admin', 'teacher1', 'teacher2', 'student1', 'student2', 'student3');


