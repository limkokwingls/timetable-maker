package com.breakoutms.timetable.service;

import java.util.Objects;

import com.breakoutms.timetable.db.DAO;
import com.breakoutms.timetable.model.beans.Lecturer;
import com.breakoutms.timetable.model.beans.LecturerIndex;

public class LecturerService {

    private final DAO<Lecturer> dao = new DAO<>(Lecturer.class);

    public LecturerIndex createIndex(Lecturer lecturer){
        return new LecturerIndex(findOrCreate(lecturer));
    }

    public Lecturer findOrCreate(Lecturer lecturer) {
        var found = dao.all()
                .stream()
                .filter(it -> Objects.nonNull(it.getName()))
                .filter(it -> it.getName().equals(lecturer.getName()))
                .findFirst().orElse(null);
        if(found == null){
            found = dao.save(lecturer);
        }
        return found;
    }
}
