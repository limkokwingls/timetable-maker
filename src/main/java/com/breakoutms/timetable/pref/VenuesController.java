package com.breakoutms.timetable.pref;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.breakoutms.timetable.model.beans.Venue;
import org.apache.commons.lang3.StringUtils;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import lombok.Getter;

@Getter
public class VenuesController extends ItemsListController<Venue> {

    @FXML private TextField venue;
    @FXML private ComboBox<Venue.VenueType> venueType;
    
    private VBox pane = new VBox();
    
    @Override
	protected void postInit() {
    	List<Venue.VenueType> venueTypes = Arrays.stream(Venue.VenueType.values())
    			.collect(Collectors.toCollection(ArrayList::new));
    	venueTypes.remove(Venue.VenueType.ANY);
		var items = FXCollections.observableArrayList(venueTypes);
		venueType.getItems().addAll(items);
    }
    
	public VenuesController() {
		super(Venue.class);
		final URL fxml = getClass().getResource("venues.fxml");
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
		venue.setText("");
		venueType.getSelectionModel().clearSelection();
	}
	
    protected List<Venue> createBean() {
    	String venueValue = venue.getText();
    	Venue.VenueType typeValue = venueType.getValue();
    	if(StringUtils.isBlank(venueValue) 
    			|| typeValue == null) {
    		return Arrays.asList();
    	}
		return Arrays.asList(new Venue(venueValue, typeValue));
	}
}
