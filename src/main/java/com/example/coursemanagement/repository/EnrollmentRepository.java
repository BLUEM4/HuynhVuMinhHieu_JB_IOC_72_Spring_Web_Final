package com.example.coursemanagement.repository;

import com.example.coursemanagement.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {
    Optional<Enrollment> findByStudent_UserIdAndCourse_CourseId(Integer studentId, Integer courseId);
    boolean existsByStudent_UserIdAndCourse_CourseId(Integer studentId, Integer courseId);
    List<Enrollment> findByStudent_UserId(Integer studentId);
    List<Enrollment> findByCourse_CourseId(Integer courseId);
}