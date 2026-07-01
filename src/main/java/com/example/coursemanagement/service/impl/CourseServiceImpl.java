package com.example.coursemanagement.service.impl;

import com.example.coursemanagement.config.AppException;
import com.example.coursemanagement.config.PagedResponse;
import com.example.coursemanagement.dto.request.CreateCourseRequest;
import com.example.coursemanagement.dto.request.UpdateCourseRequest;
import com.example.coursemanagement.dto.response.CourseResponse;
import com.example.coursemanagement.entity.Course;
import com.example.coursemanagement.entity.User;
import com.example.coursemanagement.entity.enums.CourseStatus;
import com.example.coursemanagement.entity.enums.UserRole;
import com.example.coursemanagement.mapper.CourseMapper;
import com.example.coursemanagement.repository.CourseRepository;
import com.example.coursemanagement.repository.UserRepository;
import com.example.coursemanagement.service.CourseService;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final CourseMapper courseMapper;

    @Override
    public PagedResponse<CourseResponse> getCourses(String search, Integer teacherId,
                                                    CourseStatus status, Pageable pageable) {

        Specification<Course> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Chỉ fetch teacher khi đây là query lấy dữ liệu thật (không phải count query của Pageable)
            if (query.getResultType() != Long.class && query.getResultType() != long.class) {
                root.fetch("teacher", JoinType.LEFT);
            }

            if (search != null && !search.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("title")), "%" + search.trim().toLowerCase() + "%"));
            }
            if (teacherId != null) {
                predicates.add(cb.equal(root.get("teacher").get("userId"), teacherId));
            }
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            if (query.getResultType() != Long.class && query.getResultType() != long.class) {
                query.orderBy(cb.desc(root.get("createdAt")));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<CourseResponse> page = courseRepository.findAll(spec, pageable)
                .map(courseMapper::toResponse);

        return PagedResponse.of(page);
    }

    @Override
    public CourseResponse getCourseById(Integer id) {
        Course course = courseRepository.findByIdWithTeacher(id)
                .orElseThrow(() -> AppException.notFound("Course không tồn tại"));
        return courseMapper.toResponse(course);
    }

    @Override
    @Transactional
    public CourseResponse createCourse(CreateCourseRequest request) {
        User teacher = userRepository.findById(request.getTeacherId())
                .orElseThrow(() -> AppException.notFound("Teacher không tồn tại"));

        if (teacher.getRole() != UserRole.TEACHER && teacher.getRole() != UserRole.ADMIN)
            throw AppException.badRequest("User này không phải Teacher");

        Course course = Course.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .teacher(teacher)
                .price(request.getPrice())
                .durationHours(request.getDurationHours())
                .status(CourseStatus.DRAFT)
                .build();

        return courseMapper.toResponse(courseRepository.save(course));
    }

    @Override
    @Transactional
    public CourseResponse updateCourse(Integer id, UpdateCourseRequest request) {
        Course course = findCourseById(id);
        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setPrice(request.getPrice());
        course.setDurationHours(request.getDurationHours());
        return courseMapper.toResponse(courseRepository.save(course));
    }

    @Override
    @Transactional
    public CourseResponse updateStatus(Integer id, CourseStatus status) {
        Course course = findCourseById(id);
        course.setStatus(status);
        return courseMapper.toResponse(courseRepository.save(course));
    }

    @Override
    @Transactional
    public void deleteCourse(Integer id) {
        if (!courseRepository.existsById(id))
            throw AppException.notFound("Course không tồn tại");
        courseRepository.deleteById(id);
    }

    private Course findCourseById(Integer id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> AppException.notFound("Course không tồn tại"));
    }
}