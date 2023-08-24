package com.breakoutms.timetable;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import com.breakoutms.timetable.model.beans.Project;
import com.breakoutms.timetable.model.beans.Slot;
import com.breakoutms.timetable.service.ProjectFileManager;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class ShiftSlots extends Application {

    private final ProjectFileManager projectFile = new ProjectFileManager();
	private static Stage primaryStage;

	@Override
	public void start(Stage stage) throws IOException {
		Scene scene = new Scene(new HBox(), 200, 200);
		stage.setScene(scene);
		maximizedWindow(stage);
		primaryStage = stage;
		stage.show();

        Platform.runLater(() -> {
			updateSlots();
        });
	}

	public static void main(String[] args) {
		launch(args);
	}

	private void updateSlots(){
		var project = projectFile.openFile().get();
		Project.INSTANCE.getSlots().clear();
		Project.INSTANCE.getSlots().addAll(project.getSlots());
		System.out.println("Processing...");
		for (var slot: project.getSlots()){
			var index = slot.getTimeIndex();
			if(index == 0){
				slot.setTimeIndex(0);
			}
			else if (index == 1){
				slot.setTimeIndex(1);
			}
			else if (index == 2){
				slot.setTimeIndex(2);
			}
			else if (index == 3){
				slot.setTimeIndex(4);
			}
			else if (index == 4){
				slot.setTimeIndex(5);
			}
			else if (index == 5){
				slot.setTimeIndex(6);
			}
			else if (index == 6){
				slot.setTimeIndex(8);
			}
			else if (index ==7){
				slot.setTimeIndex(9);
			}
			else if (index == 8){
				slot.setTimeIndex(10);
			}
			else if (index == 9){
				slot.setTimeIndex(12);
			}
			else if (index == 10){
				slot.setTimeIndex(13);
			}
			else if (index == 11){
				slot.setTimeIndex(14);
			}
			else if (index == 12){
				slot.setTimeIndex(16);
			}
			else if (index == 13){
				slot.setTimeIndex(17);
			}
			else if (index == 14){
				slot.setTimeIndex(18);
			}
		}
		System.out.println("Done");
		projectFile.saveFile();
		Platform.exit();
	}

	private void maximizedWindow(final Stage stage) {
		final Screen screen = Screen.getPrimary();
		final Rectangle2D bounds = screen.getVisualBounds();
		stage.setWidth(bounds.getWidth());
		stage.setHeight(bounds.getHeight());
		stage.setMaximized(true);
	}
	
	public static Stage getPrimaryStage(){
		return primaryStage;
	}
}

