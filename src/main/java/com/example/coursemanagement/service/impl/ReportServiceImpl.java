package com.example.coursemanagement.service.impl;

import com.example.coursemanagement.config.AppException;
import com.example.coursemanagement.dto.response.*;
import com.example.coursemanagement.entity.*;
import com.example.coursemanagement.entity.enums.EnrollmentStatus;
import com.example.coursemanagement.entity.enums.UserRole;
import com.example.coursemanagement.repository.*;
import com.example.coursemanagement.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ReportTopCourseResponse> getTopCourses() {
        return courseRepository.findAll().stream()
                .map(course -> {
                    long count = enrollmentRepository
                            .findByCourse_CourseId(course.getCourseId()).size();
                    return new ReportTopCourseResponse(
                            course.getCourseId(),
                            course.getTitle(),
                            course.getTeacher().getFullName(),
                            count,
                            course.getStatus().name()
                    );
                })
                .sorted((a, b) -> Long.compare(b.getEnrollmentCount(), a.getEnrollmentCount()))
                .limit(10)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ReportStudentProgressResponse getStudentProgress(Integer studentId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> AppException.notFound("Student không tồn tại"));

        if (student.getRole() != UserRole.STUDENT) {
            throw AppException.badRequest("User này không phải STUDENT");
        }

        List<Enrollment> enrollments = enrollmentRepository
                .findByStudent_UserId(studentId);

        int completed = (int) enrollments.stream()
                .filter(e -> e.getStatus() == EnrollmentStatus.COMPLETED)
                .count();

        List<ReportStudentProgressResponse.CourseProgressItem> items = enrollments.stream()
                .map(e -> new ReportStudentProgressResponse.CourseProgressItem(
                        e.getCourse().getCourseId(),
                        e.getCourse().getTitle(),
                        e.getProgressPercentage(),
                        e.getStatus().name()
                )).toList();

        return new ReportStudentProgressResponse(
                student.getUserId(),
                student.getFullName(),
                enrollments.size(),
                completed,
                items
        );
    }

    @Override
    @Transactional(readOnly = true)
    public ReportTeacherOverviewResponse getTeacherOverview(Integer teacherId) {
        User teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> AppException.notFound("Teacher không tồn tại"));

        if (teacher.getRole() != UserRole.TEACHER && teacher.getRole() != UserRole.ADMIN) {
            throw AppException.badRequest("User này không phải TEACHER");
        }

        List<Course> courses = courseRepository.findAll().stream()
                .filter(c -> c.getTeacher().getUserId().equals(teacherId))
                .toList();

        int totalStudents = courses.stream()
                .mapToInt(c -> enrollmentRepository
                        .findByCourse_CourseId(c.getCourseId()).size())
                .sum();

        List<ReportTeacherOverviewResponse.CourseOverviewItem> items = courses.stream()
                .map(c -> {
                    long count = enrollmentRepository
                            .findByCourse_CourseId(c.getCourseId()).size();
                    return new ReportTeacherOverviewResponse.CourseOverviewItem(
                            c.getCourseId(),
                            c.getTitle(),
                            c.getStatus().name(),
                            count
                    );
                }).toList();

        return new ReportTeacherOverviewResponse(
                teacher.getUserId(),
                teacher.getFullName(),
                courses.size(),
                totalStudents,
                items
        );
    }
}