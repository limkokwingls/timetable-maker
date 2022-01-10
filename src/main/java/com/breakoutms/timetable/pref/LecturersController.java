package com.breakoutms.timetable.pref;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

import com.breakoutms.timetable.model.beans.Lecturer;
import org.apache.commons.lang3.StringUtils;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import lombok.Getter;

@Getter
public class LecturersController extends ItemsListController<Lecturer> {

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
		names.setText("");
	}
	
    protected List<Lecturer> createBean() {
    	String value = names.getText();
    	if(StringUtils.isBlank(value)) {
    		return Arrays.asList();
    	}
		return Arrays.asList(new Lecturer(value));
	}
}
