package com.example.coursemanagement.service.impl;

import com.example.coursemanagement.config.AppException;
import com.example.coursemanagement.dto.request.CreateLessonRequest;
import com.example.coursemanagement.dto.request.UpdateLessonRequest;
import com.example.coursemanagement.dto.response.LessonResponse;
import com.example.coursemanagement.entity.Course;
import com.example.coursemanagement.entity.Lesson;
import com.example.coursemanagement.entity.User;
import com.example.coursemanagement.entity.enums.UserRole;
import com.example.coursemanagement.mapper.LessonMapper;
import com.example.coursemanagement.repository.CourseRepository;
import com.example.coursemanagement.repository.LessonRepository;
import com.example.coursemanagement.repository.UserRepository;
import com.example.coursemanagement.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final LessonMapper lessonMapper;

    @Override
    @Transactional(readOnly = true)
    public List<LessonResponse> getLessonsByCourse(Integer courseId, String username) {
        findCourseById(courseId);
        User user = findUserByUsername(username);

        if (user.getRole() == UserRole.STUDENT) {
            return lessonRepository
                    .findByCourse_CourseIdAndIsPublishedOrderByOrderIndex(courseId, true)
                    .stream()
                    .map(lessonMapper::toResponse)
                    .toList();
        }

        return lessonRepository
                .findByCourse_CourseIdOrderByOrderIndex(courseId)
                .stream()
                .map(lessonMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public LessonResponse getLessonById(Integer lessonId, String username) {
        Lesson lesson = findLessonById(lessonId);
        User user = findUserByUsername(username);

        if (user.getRole() == UserRole.STUDENT && !lesson.isPublished()) {
            throw AppException.forbidden("Lesson chưa được published");
        }

        return lessonMapper.toResponse(lesson);
    }

    @Override
    @Transactional
    public LessonResponse createLesson(Integer courseId, CreateLessonRequest request,
                                       String username) {
        Course course = findCourseById(courseId);
        checkOwnership(course, username);

        Lesson lesson = Lesson.builder()
                .course(course)
                .title(request.getTitle())
                .contentUrl(request.getContentUrl())
                .textContent(request.getTextContent())
                .orderIndex(request.getOrderIndex())
                .isPublished(false)
                .build();

        return lessonMapper.toResponse(lessonRepository.save(lesson));
    }

    @Override
    @Transactional
    public LessonResponse updateLesson(Integer lessonId, UpdateLessonRequest request,
                                       String username) {
        Lesson lesson = findLessonById(lessonId);
        checkOwnership(lesson.getCourse(), username);

        lesson.setTitle(request.getTitle());
        lesson.setContentUrl(request.getContentUrl());
        lesson.setTextContent(request.getTextContent());
        lesson.setOrderIndex(request.getOrderIndex());

        return lessonMapper.toResponse(lessonRepository.save(lesson));
    }

    @Override
    @Transactional
    public LessonResponse publishLesson(Integer lessonId, String username) {
        Lesson lesson = findLessonById(lessonId);
        checkOwnership(lesson.getCourse(), username);
        lesson.setPublished(true);
        return lessonMapper.toResponse(lessonRepository.save(lesson));
    }

    @Override
    @Transactional
    public void deleteLesson(Integer lessonId, String username) {
        Lesson lesson = findLessonById(lessonId);
        checkOwnership(lesson.getCourse(), username);
        lessonRepository.deleteById(lessonId);
    }



    private Course findCourseById(Integer courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> AppException.notFound("Course không tồn tại"));
    }

    private Lesson findLessonById(Integer lessonId) {
        return lessonRepository.findById(lessonId)
                .orElseThrow(() -> AppException.notFound("Lesson không tồn tại"));
    }

    private User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> AppException.notFound("User không tồn tại"));
    }

    // TEACHER chỉ được thao tác lesson của course mình phụ trách
    private void checkOwnership(Course course, String username) {
        User user = findUserByUsername(username);
        if (user.getRole() == UserRole.ADMIN) return; // ADMIN bypass
        if (!course.getTeacher().getUsername().equals(username)) {
            throw AppException.forbidden("Bạn không có quyền thao tác lesson này");
        }
    }
}