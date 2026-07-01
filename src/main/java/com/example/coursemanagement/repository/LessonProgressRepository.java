package com.example.coursemanagement.repository;

import com.example.coursemanagement.entity.LessonProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface LessonProgressRepository extends JpaRepository<LessonProgress, Integer> {
    Optional<LessonProgress> findByEnrollment_EnrollmentIdAndLesson_LessonId(
            Integer enrollmentId, Integer lessonId);
    List<LessonProgress> findByEnrollment_EnrollmentId(Integer enrollmentId);
    long countByEnrollment_EnrollmentIdAndIsCompleted(Integer enrollmentId, boolean isCompleted);
}