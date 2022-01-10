package com.breakoutms.timetable.ui;

import com.breakoutms.timetable.model.beans.Slot;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class SlotTable {

	private SlotTable(){}

	public static TableView<Slot> get() {
	    TableView<Slot> tableView = new TableView<>();

	    TableColumn<Slot, String>course = new TableColumn<>("Course");
	    course.setPrefWidth(250);
	    course.setCellValueFactory(new PropertyValueFactory<>("course"));
	    
	    TableColumn<Slot, String> studentClass = new TableColumn<>("Class");
	    studentClass.setPrefWidth(100);
	    studentClass.setCellValueFactory(new PropertyValueFactory<>("studentClassName"));

		TableColumn<Slot, String> venue = new TableColumn<>("Venue");
		venue.setPrefWidth(70);
		venue.setCellValueFactory(new PropertyValueFactory<>("venueName"));

		TableColumn<Slot, String> time = new TableColumn<>("Time");
		time.setPrefWidth(190);
		time.setCellValueFactory(new PropertyValueFactory<>("time"));

	    tableView.getColumns().add(course);
	    tableView.getColumns().add(studentClass);
		tableView.getColumns().add(venue);
		tableView.getColumns().add(time);
	    
		return tableView;
	}

}
