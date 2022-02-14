package com.breakoutms.timetable.pref;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.breakoutms.timetable.Main;
import com.breakoutms.timetable.MainController;
import com.breakoutms.timetable.model.beans.Lecturer;
import com.breakoutms.timetable.model.beans.StudentClass;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import javafx.scene.control.ComboBox;
import javafx.stage.FileChooser;
import org.apache.commons.lang3.StringUtils;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import lombok.Getter;

@Getter
public class StudentClassesController extends ItemsListController<StudentClass> {

	@FXML private TextField name;
    private final VBox pane = new VBox();
    
	public StudentClassesController() {
		super(StudentClass.class);
		final URL fxml = getClass().getResource("student_classes.fxml");
		final FXMLLoader loader = new FXMLLoader(fxml);
		loader.setController(this);
		loader.setRoot(pane);
		try {
			loader.load();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	protected void clear() {
		name.setText("");
	}

	protected StudentClass createBean() {
		String value = name.getText();
		if(StringUtils.isBlank(value)) {
			return null;
		}
		return new StudentClass(value);
	}

	@FXML
	public void importData() throws IOException, CsvException {
		FileChooser fileChooser = new FileChooser();
		File file = fileChooser.showOpenDialog(Main.getPrimaryStage());

		if (file != null) {
			List<StudentClass> students = new ArrayList<>();
			try (CSVReader reader = new CSVReader(new FileReader(file))) {
				List<String[]> rows = reader.readAll();
				for (String[] row : rows) {
					try{
						String name = row[0];
						if(!StringUtils.isBlank(name)) {
							var studentClass = new StudentClass(name.toUpperCase().trim());
							if(!students.contains(studentClass)) {
								students.add(studentClass);
							}
						}
					}catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			students.forEach(item -> {
				try {
					dao.save(item);
					listView.getItems().add(item);
				}catch (Exception e) {
					System.err.println("Error while saving class: " + item.toString() + ", "+ e.toString());
				}
			});
			System.out.println("Done importing classes");

		}
	}

	@Override
	protected ComboBox<StudentClass> comboBox() {
		return MainController.studentClassComboBox();
	}
}
