package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        System.out.println("JavaFX start() method called!");
        
        Label label = new Label("🎉 SUCCESS! JavaFX is working! 🎉");
        StackPane root = new StackPane(label);
        Scene scene = new Scene(root, 400, 200);
        
        primaryStage.setTitle("Car Rental System - JavaFX Working");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        System.out.println("=== Application Starting ===");
        System.out.println("Java Version: " + System.getProperty("java.version"));
        
        // Check if JavaFX classes are available
        try {
            Class.forName("javafx.application.Application");
            System.out.println("✅ JavaFX Application class found");
        } catch (ClassNotFoundException e) {
            System.out.println("❌ JavaFX Application class NOT found");
        }
        
        // Launch JavaFX
        launch(args);
    }
}