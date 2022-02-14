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
import com.breakoutms.timetable.model.beans.Course;
import com.breakoutms.timetable.model.beans.Lecturer;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import javafx.event.ActionEvent;
import javafx.stage.FileChooser;
import org.apache.commons.lang3.StringUtils;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import lombok.Getter;
import org.controlsfx.control.SearchableComboBox;

@Getter
public class LecturersController extends ItemsListController<Lecturer> {

    @FXML private TextField title;
    @FXML private TextField names;
    private final VBox pane = new VBox();
    
	public LecturersController() {
		super(Lecturer.class);
		final URL fxml = getClass().getResource("lecturers.fxml");
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
		title.setText("");
		names.setText("");
	}

	protected Lecturer createBean() {
    	String namesValue = names.getText();
		String titleValue = title.getText();
    	if(StringUtils.isBlank(namesValue)) {
    		return null;
    	}
		if(!StringUtils.isBlank(titleValue)) {
			titleValue = title.getText().trim();
		}
		return new Lecturer(titleValue, namesValue);
	}

	@FXML
	public void importData() throws IOException, CsvException {
		FileChooser fileChooser = new FileChooser();

		File file = fileChooser.showOpenDialog(Main.getPrimaryStage());

		if (file != null) {
			List<Lecturer> lecturers = new ArrayList<>();
			try (CSVReader reader = new CSVReader(new FileReader(file))) {
				List<String[]> rows = reader.readAll();
				for (String[] row : rows) {
					try{
						String name = row[0];
						if(!StringUtils.isBlank(name)) {
							Lecturer lecturer = new Lecturer(name.trim(), row[1].trim());
							if(!lecturers.contains(lecturer)) {
								lecturers.add(lecturer);
							}
						}
					}catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			lecturers.forEach(item -> {
				try {
					dao.save(item);
					listView.getItems().add(item);
				}catch (Exception e) {
					System.err.println("Error while saving course: " + item.toString());
				}
			});
			System.out.println("Done importing lecturers");

		}
	}

	@Override
	protected SearchableComboBox<Lecturer> comboBox() {
		return MainController.lecturesComboBox();
	}
}
