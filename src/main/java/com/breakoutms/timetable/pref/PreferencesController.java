package com.breakoutms.timetable.pref;

import java.net.URL;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;

public class PreferencesController extends DialogPane {

    @FXML private ListView<String> listView;
    @FXML private TitledPane contentPane;
    
	private final ObservableList<String> items = FXCollections.observableArrayList(
			"Lecturers",
			"Causes",
			"Classes",
			"Venues"
	);
    
	public PreferencesController() {
		final URL fxml = getClass().getResource("preferences.fxml");
		final FXMLLoader loader = new FXMLLoader(fxml);
		loader.setController(this);
		loader.setRoot(this);
		try {
			loader.load();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
	
    @FXML
    void initialize() {
    	listView.setItems(items);
    	listView.getSelectionModel()
    		.selectedIndexProperty()
    		.addListener(this::listItemSelected);
		contentPane.setText(items.get(0));
		contentPane.setContent(new LecturersController().getPane());
    }
    
	public void listItemSelected(ObservableValue<? extends Number> observable, 
			Number oldValue, Number newValue) {
		if(newValue == null) return;
		contentPane.setText(items.get(newValue.intValue()));
		
		if(newValue.intValue() == 0) {
			contentPane.setContent(new LecturersController().getPane());
		}
		if(newValue.intValue() == 1) {
			contentPane.setContent(new CoursesController().getPane());
		}
		if(newValue.intValue() == 2) {
			contentPane.setContent(new StudentClassesController().getPane());
		}
		if(newValue.intValue() == 3) {
			contentPane.setContent(new VenuesController().getPane());
		}
	}
}
