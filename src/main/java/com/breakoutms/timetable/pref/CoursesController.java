package com.breakoutms.timetable.pref;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

import com.breakoutms.timetable.model.beans.Course;
import org.apache.commons.lang3.StringUtils;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import lombok.Getter;

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
	
    protected List<Course> createBean() {
    	String nameValue = this.name.getText();
    	String codeValue = this.code.getText();
    	if(StringUtils.isBlank(nameValue) || StringUtils.isBlank(codeValue)) {
    		return Arrays.asList();
    	}
		return Arrays.asList(new Course(nameValue, codeValue));
	}
}
