package com.breakoutms.timetable.service;

import java.util.Objects;

import com.breakoutms.timetable.db.DAO;
import com.breakoutms.timetable.model.beans.StudentClass;
import com.breakoutms.timetable.model.beans.StudentClassIndex;

public class StudentClassService {

    private final DAO<StudentClass> dao = new DAO<>(StudentClass.class);

    public StudentClassIndex createIndex(StudentClass studentClass){
        return new StudentClassIndex(findOrCreate(studentClass));
    }

    public StudentClass findOrCreate(StudentClass studentClass) {
        var found = dao.all()
                .stream().filter(it -> Objects.nonNull(it.getName()))
                .filter(it -> it.getName().equals(studentClass.getName()))
                .findFirst().orElse(null);
        if(found == null){
            found = dao.save(studentClass);
        }
        return found;
    }
}
