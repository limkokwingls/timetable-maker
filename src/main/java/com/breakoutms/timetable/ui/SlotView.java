package com.breakoutms.timetable.ui;

import com.breakoutms.timetable.MainController;
import com.breakoutms.timetable.model.beans.Lecturer;
import com.breakoutms.timetable.model.beans.Slot;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import lombok.Getter;
import org.controlsfx.control.SearchableComboBox;

@Getter
public abstract class SlotView extends BorderPane {
	
	public static final String BORDER_COLOR = "#bdbdbd";
	public static final String BACKGROUND_COLOR = "#EEEEEE;";
	
	protected Label topLabel = new Label();
	protected Label leftLabel = new Label();
	protected Label rightLabel = new Label();
	private final Slot slot;
	private final int row;
	private final int col;
	
	protected SlotView(Slot slot) {
		this.slot = slot;
		row = slot.row();
		col = slot.col();
		setTop(topLabel);
		setLeft(leftLabel);
		setRight(rightLabel);
		setProperties(slot);
		setupUI();
	}
	
	private void setupUI() {
		setPadding(new Insets(5));
		BorderPane.setAlignment(topLabel, Pos.CENTER);
		String family = Font.getDefault().getFamily();
		Font font = Font.font(family, 10);
		rightLabel.setFont(font);
		leftLabel.setFont(font);
		topLabel.setFont(Font.font(family, 10));
		topLabel.setPadding(new Insets(5));
		String style = "-fx-background-color: "+ BACKGROUND_COLOR  + "; "+
				String.format("-fx-border-color: %s;", BORDER_COLOR);
		setStyle(style);
		setCursor(Cursor.HAND);
		setOnMouseClicked(e -> {
			MainController mainController = MainController.getInstance();
			SearchableComboBox<Lecturer> lecturersFilter = mainController.getLecturersFilter();
			lecturersFilter.getSelectionModel().select(slot.getLecturer());
			var allocationTable = mainController.getSlotTableView();
			allocationTable.getSelectionModel().select(slot);
			Platform.runLater(allocationTable::requestFocus);
		});
	}
	
	protected abstract void setProperties(Slot slot);
	
	@Override
	public String toString() {
		String format = "Slot at col=%d & row=%d";
		return String.format(format, col, row);
	}
}
