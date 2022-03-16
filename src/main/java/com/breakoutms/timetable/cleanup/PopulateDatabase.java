package com.breakoutms.timetable.cleanup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

import com.breakoutms.timetable.db.DAO;
import com.breakoutms.timetable.model.beans.Course;
import com.breakoutms.timetable.model.beans.Lecturer;
import com.breakoutms.timetable.model.beans.Project;

public class PopulateDatabase {
    
    static DAO<Course> courseDAO = new DAO<Course>(Course.class);
    static DAO<Lecturer> lecturerDAO = new DAO<Lecturer>(Lecturer.class);
    
    
    public static void main(String[] args) throws Exception {
        Project project = readProjectFile();
        var courses =  project.getSlots().stream().map(slot -> slot.getCourse()).distinct().toArray(Course[]::new);
        System.out.println("Saving " + courses.length + " courses");
        for(Course course : courses) {
            System.out.println("Saving " + course.getName());
            course.setId(null);
            courseDAO.save(course);
        }
        var lecturers = project.getSlots().stream().map(slot -> slot.getLecturer()).distinct().toArray(Lecturer[]::new);
        System.out.println("\n\nSaving " + lecturers.length + " lecturers");
        for(Lecturer lecturer : lecturers) {
            System.out.println("Saving "+ lecturer.getName());
            lecturer.setId(null);
            lecturerDAO.save(lecturer);
        }
    }

    private static Project readProjectFile() throws Exception {
        File path = new File("C:/Users/Ntholi Nkhatho/Documents/Limkokwing/Timetable/Timetable 2022-02/timetable_14_02_2020 - 40.ttb");
        try (ObjectInputStream is = new ObjectInputStream(new FileInputStream(path))) {
            return (Project) is.readObject();
        }
    }


}
