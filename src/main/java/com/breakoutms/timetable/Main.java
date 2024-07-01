package com.breakoutms.timetable;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;


public class Main extends Application {

	private static Stage primaryStage;

	@Override
	public void start(Stage stage) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/breakoutms/timetable/main.fxml"));		Scene scene = new Scene(fxmlLoader.load(), 990, 800);
		scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
		stage.setScene(scene);
		maximizedWindow(stage);
		primaryStage = stage;
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
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
