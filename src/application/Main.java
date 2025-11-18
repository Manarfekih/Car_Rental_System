package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/dashboard.fxml"));
            primaryStage.setTitle("Car Rental Management System");
            primaryStage.setScene(new Scene(root, 1000, 700));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            // Fallback to simple scene if FXML not found
            primaryStage.setTitle("Car Rental System - Welcome");
            primaryStage.setScene(new Scene(new javafx.scene.control.Label("Car Rental System Loading..."), 300, 200));
            primaryStage.show();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
