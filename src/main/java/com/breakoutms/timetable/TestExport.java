package com.breakoutms.timetable;

import com.breakoutms.timetable.model.beans.Project;
import com.breakoutms.timetable.service.ProjectFileManager;
import com.breakoutms.timetable.service.WordExporter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class TestExport {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Project project = readFile();
        WordExporter.export(project.getSlots());
        System.out.println("Done!");
    }

    private static Project readFile()
            throws IOException, ClassNotFoundException {
        String file = "/home/ntholi/Downloads/test_final.ttb";
        try (ObjectInputStream is = new ObjectInputStream(new FileInputStream(file))) {
            return (Project) is.readObject();
        }
    }
}
