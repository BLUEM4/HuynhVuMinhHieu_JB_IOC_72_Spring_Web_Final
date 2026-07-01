package com.example.coursemanagement.repository;

import com.example.coursemanagement.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByCourse_CourseId(Integer courseId);
    Optional<Review> findByCourse_CourseIdAndStudent_UserId(Integer courseId, Integer studentId);
    boolean existsByCourse_CourseIdAndStudent_UserId(Integer courseId, Integer studentId);
}