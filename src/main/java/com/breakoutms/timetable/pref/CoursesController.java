package com.breakoutms.timetable.pref;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.breakoutms.timetable.Main;
import com.breakoutms.timetable.MainController;
import com.breakoutms.timetable.model.beans.Course;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import org.apache.commons.lang3.StringUtils;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import lombok.Getter;
import org.controlsfx.control.SearchableComboBox;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

@Getter
public class CoursesController extends ItemsListController<Course> {

    @FXML private TextField name;
    @FXML private TextField code;
    private VBox pane = new VBox();
    
	public CoursesController() {
		super(Course.class);
		final URL fxml = getClass().getResource("courses.fxml");
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
		code.setText("");
	}
	
    protected Course createBean() {
    	String nameValue = this.name.getText();
    	String codeValue = this.code.getText();
    	if(StringUtils.isBlank(nameValue) || StringUtils.isBlank(codeValue)) {
    		return null;
    	}
		return new Course(nameValue, codeValue);
	}

	@FXML
	public void importData() throws IOException, CsvException {
		FileChooser fileChooser = new FileChooser();

		File file = fileChooser.showOpenDialog(Main.getPrimaryStage());

		if (file != null) {
			List<Course> courses = new ArrayList<>();
			try (CSVReader reader = new CSVReader(new FileReader(file))) {
				List<String[]> rows = reader.readAll();
				for (String[] row : rows) {
					try{
						String name = row[0];
						if(!StringUtils.isBlank(name)) {
							Course course = new Course(name.trim(), row[1].trim());
							if(!courses.contains(course)) {
								courses.add(course);
							}
						}
					}catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			courses.forEach(course -> {
				try {
					dao.save(course);
					listView.getItems().add(course);
				}catch (Exception e) {
					System.err.println("Error while saving course: " + course.toString() + ", "+ e.toString());
				}
			});
			System.out.println("Done importing courses");

		}
	}

	@Override
	protected SearchableComboBox<Course> comboBox() {
		return MainController.courseComboBox();
	}
}
