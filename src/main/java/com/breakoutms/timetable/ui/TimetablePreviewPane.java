package com.breakoutms.timetable.ui;

import com.breakoutms.timetable.model.beans.Slot;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;
import java.util.List;

public class TimetablePreviewPane extends VBox {

	private final TimetableGrid grid = new TimetableGrid();

	public TimetablePreviewPane(String titleText) {
		Label title = new Label();
		title.setFont(Font.font(grid.fontFamily(), FontWeight.NORMAL, 16));
		title.setPadding(new Insets(8));
		title.setText(titleText);
		grid.setPrefHeight(150);
		getChildren().addAll(title, new Separator(), grid, new Separator());
	}

	public void add(SlotView slotView) {
		//+1 is added because of timetable row and column labels
		grid.add(slotView, slotView.getCol()+1, slotView.getRow()+1);
	}

	public void delete(Slot slot){
		var children = grid.getChildren();
		List<Node> toRemove = new ArrayList<>();
		Node nodeToDelete = null;
		for (var child: children) {
			if(child instanceof SlotView slotView){
				Slot match = slotView.getSlot();
				if(match == slot){
					toRemove.add(child);
				}
			}
		}
		children.removeAll(toRemove);
	}
}
