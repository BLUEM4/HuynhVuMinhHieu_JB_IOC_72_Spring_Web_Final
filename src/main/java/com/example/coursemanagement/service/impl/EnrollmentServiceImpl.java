package com.example.coursemanagement.service.impl;

import com.example.coursemanagement.config.AppException;
import com.example.coursemanagement.dto.request.CreateEnrollmentRequest;
import com.example.coursemanagement.dto.response.CourseResponse;
import com.example.coursemanagement.dto.response.EnrollmentResponse;
import com.example.coursemanagement.dto.response.EnrollmentResponse.LessonProgressResponse;
import com.example.coursemanagement.dto.response.UserResponse;
import com.example.coursemanagement.entity.*;
import com.example.coursemanagement.entity.enums.CourseStatus;
import com.example.coursemanagement.entity.enums.EnrollmentStatus;
import com.example.coursemanagement.entity.enums.UserRole;
import com.example.coursemanagement.mapper.CourseMapper;
import com.example.coursemanagement.mapper.UserMapper;
import com.example.coursemanagement.repository.*;
import com.example.coursemanagement.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final LessonRepository lessonRepository;
    private final LessonProgressRepository lessonProgressRepository;
    private final UserMapper userMapper;
    private final CourseMapper courseMapper;

    @Override
    public List<EnrollmentResponse> getMyEnrollments(String username) {
        User student = findUserByUsername(username);
        return enrollmentRepository.findByStudent_UserId(student.getUserId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public EnrollmentResponse getEnrollmentById(Integer enrollmentId, String username) {
        Enrollment enrollment = findEnrollmentById(enrollmentId);
        User user = findUserByUsername(username);

        // Chỉ được xem enrollment của chính mình
        if (!enrollment.getStudent().getUserId().equals(user.getUserId())) {
            throw AppException.forbidden("Bạn không có quyền xem enrollment này");
        }

        return toResponse(enrollment);
    }

    @Override
    @Transactional
    public EnrollmentResponse enroll(CreateEnrollmentRequest request, String username) {
        User student = findUserByUsername(username);

        // Chỉ STUDENT mới được enroll
        if (student.getRole() != UserRole.STUDENT) {
            throw AppException.forbidden("Chỉ STUDENT mới được đăng ký khóa học");
        }

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> AppException.notFound("Course không tồn tại"));

        // Chỉ được đăng ký course PUBLISHED
        if (course.getStatus() != CourseStatus.PUBLISHED) {
            throw AppException.badRequest("Khóa học này chưa được phát hành");
        }

        // Kiểm tra đã đăng ký chưa
        if (enrollmentRepository.existsByStudent_UserIdAndCourse_CourseId(
                student.getUserId(), course.getCourseId())) {
            throw AppException.conflict("Bạn đã đăng ký khóa học này rồi");
        }

        Enrollment enrollment = Enrollment.builder()
                .student(student)
                .course(course)
                .status(EnrollmentStatus.ENROLLED)
                .progressPercentage(BigDecimal.ZERO)
                .build();

        return toResponse(enrollmentRepository.save(enrollment));
    }

    @Override
    @Transactional
    public EnrollmentResponse completeLesson(Integer enrollmentId, Integer lessonId, String username) {
        Enrollment enrollment = findEnrollmentById(enrollmentId);
        User user = findUserByUsername(username);

        // Chỉ được cập nhật enrollment của chính mình
        if (!enrollment.getStudent().getUserId().equals(user.getUserId())) {
            throw AppException.forbidden("Bạn không có quyền cập nhật enrollment này");
        }

        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> AppException.notFound("Lesson không tồn tại"));

        // Lesson phải thuộc course của enrollment
        if (!lesson.getCourse().getCourseId().equals(enrollment.getCourse().getCourseId())) {
            throw AppException.badRequest("Lesson không thuộc khóa học này");
        }


        LessonProgress progress = lessonProgressRepository
                .findByEnrollment_EnrollmentIdAndLesson_LessonId(enrollmentId, lessonId)
                .orElse(LessonProgress.builder()
                        .enrollment(enrollment)
                        .lesson(lesson)
                        .build());

        progress.setCompleted(true);
        progress.setCompletedAt(LocalDateTime.now());
        lessonProgressRepository.save(progress);


        long totalPublished = lessonRepository
                .countByCourse_CourseIdAndIsPublished(enrollment.getCourse().getCourseId(), true);
        long completedCount = lessonProgressRepository
                .countByEnrollment_EnrollmentIdAndIsCompleted(enrollmentId, true);

        if (totalPublished > 0) {
            BigDecimal percentage = BigDecimal.valueOf(completedCount)
                    .divide(BigDecimal.valueOf(totalPublished), 2, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
            enrollment.setProgressPercentage(percentage);

            // Nếu hoàn thành 100% → COMPLETED
            if (percentage.compareTo(BigDecimal.valueOf(100)) >= 0) {
                enrollment.setStatus(EnrollmentStatus.COMPLETED);
                enrollment.setCompletionDate(LocalDateTime.now());
            }
        }

        return toResponse(enrollmentRepository.save(enrollment));
    }



    private EnrollmentResponse toResponse(Enrollment e) {
        List<LessonProgressResponse> progresses = lessonProgressRepository
                .findByEnrollment_EnrollmentId(e.getEnrollmentId())
                .stream()
                .map(lp -> LessonProgressResponse.builder()
                        .lessonId(lp.getLesson().getLessonId())
                        .lessonTitle(lp.getLesson().getTitle())
                        .completed(lp.isCompleted())
                        .completedAt(lp.getCompletedAt())
                        .build())
                .toList();

        return EnrollmentResponse.builder()
                .enrollmentId(e.getEnrollmentId())
                .student(userMapper.toResponse(e.getStudent()))
                .course(courseMapper.toResponse(e.getCourse()))
                .enrollmentDate(e.getEnrollmentDate())
                .status(e.getStatus())
                .progressPercentage(e.getProgressPercentage())
                .completionDate(e.getCompletionDate())
                .lessonProgresses(progresses)
                .build();
    }

    private Enrollment findEnrollmentById(Integer id) {
        return enrollmentRepository.findById(id)
                .orElseThrow(() -> AppException.notFound("Enrollment không tồn tại"));
    }

    private User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> AppException.notFound("User không tồn tại"));
    }
}