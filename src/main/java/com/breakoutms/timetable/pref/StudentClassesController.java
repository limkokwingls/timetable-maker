package com.breakoutms.timetable.pref;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

import com.breakoutms.timetable.model.beans.StudentClass;
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

	protected List<StudentClass> createBean() {
		String value = name.getText();
		if(StringUtils.isBlank(value)) {
			return Arrays.asList();
		}
		return Arrays.asList(new StudentClass(value));
	}
}
