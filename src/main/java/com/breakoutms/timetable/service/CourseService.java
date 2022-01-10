package com.breakoutms.timetable.service;

import com.breakoutms.timetable.db.DAO;
import com.breakoutms.timetable.model.beans.Course;
import org.apache.commons.lang3.StringUtils;

public class CourseService {

    private final DAO<Course> dao = new DAO<>(Course.class);

    public Course create(Course newCourse) {
        var course = dao.all()
                .stream()
                .filter(it -> it.getName().equalsIgnoreCase(newCourse.getName()))
                .findFirst().orElse(null);
        if(course == null){
            course = dao.save(newCourse);
        }
        if(StringUtils.isBlank(course.getCode()) && StringUtils.isNoneBlank(newCourse.getCode())){
            course.setCode(newCourse.getCode());
            course = dao.save(course);
        }
        return course;
    }
}
