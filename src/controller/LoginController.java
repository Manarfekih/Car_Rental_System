package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;
import service.UserService;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Label messageLabel;

    @FXML
    private Button registerLink;

    @FXML
    public void initialize() {
        registerLink.setVisible(false); // hide register link initially
        loginButton.setOnAction(e -> login());
        registerLink.setOnAction(e -> openRegisterForm());
    }

    private void login() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Please enter username & password.");
            return;
        }

        UserService userService = new UserService();
        User user = userService.login(username, password);

        if (user != null) {
            messageLabel.setText("Login successful! Role: " + user.getRole());
            registerLink.setVisible(false);

            // Open main window and pass the logged-in user
            openMainWindow(user);

            // Close login window
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.close();
        } else {
            messageLabel.setText("User not found. Please register first.");
            registerLink.setVisible(true); // show register link/button
        }
    }

    private void openRegisterForm() {
        try {
            // Load the Register.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Register.fxml"));
            Parent root = loader.load();

            // Create new Stage for registration
            Stage stage = new Stage();
            stage.setTitle("Register");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            messageLabel.setText("Cannot open registration form.");
        }
    }

    private void openMainWindow(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Main.fxml"));
            Parent root = loader.load();

            // Pass logged-in user to MainController
            MainController mainController = loader.getController();
            mainController.setLoggedUser(user);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Car Rental System - Welcome " + user.getUsername());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            messageLabel.setText("Error opening main window.");
        }
    }
}
