package com.example.coursemanagement.repository;

import com.example.coursemanagement.entity.Course;
import com.example.coursemanagement.entity.enums.CourseStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {


    @Query("""
    select c
    from Course c
    join fetch c.teacher u
    where (:search is null or lower(c.title) like lower(cast(:search as string)))
      and (:teacherId is null or u.userId = :teacherId)
      and c.status = coalesce(:status, c.status)
    """)
    Page<Course> searchCourses(
            @Param("search") String search,
            @Param("teacherId") Integer teacherId,
            @Param("status") CourseStatus status,
            Pageable pageable
    );

    @Query("SELECT c FROM Course c LEFT JOIN FETCH c.teacher WHERE c.courseId = :id")
    Optional<Course> findByIdWithTeacher(@Param("id") Integer id);
}