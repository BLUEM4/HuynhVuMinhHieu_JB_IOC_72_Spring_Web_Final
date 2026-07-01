package com.example.coursemanagement.repository;

import com.example.coursemanagement.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Integer> {
    List<Lesson> findByCourse_CourseIdOrderByOrderIndex(Integer courseId);
    List<Lesson> findByCourse_CourseIdAndIsPublishedOrderByOrderIndex(Integer courseId, boolean isPublished);
    long countByCourse_CourseIdAndIsPublished(Integer courseId, boolean isPublished);
}