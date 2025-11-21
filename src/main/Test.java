package main;


import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
public class Test extends Application {
	@Override
	public void start(Stage stage) {
	    try {
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
	        Scene scene = new Scene(loader.load());
	        stage.setScene(scene);
	        stage.setTitle("Car Rental System - Login");
	        stage.show();
	    } catch (IOException e) {
	        e.printStackTrace(); // Log the error
	        // Show an alert to the user
	        Alert alert = new Alert(Alert.AlertType.ERROR);
	        alert.setTitle("Error");
	        alert.setHeaderText("Failed to load the login screen");
	        alert.setContentText("Please check your FXML file and try again.\n" + e.getMessage());
	        alert.showAndWait();
	    }
	}

	public static void main(String[] args) {
	    launch();
	}
	}