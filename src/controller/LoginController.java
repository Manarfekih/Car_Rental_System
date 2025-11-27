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
import java.sql.SQLException;

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

    private final UserService userService = new UserService();

    @FXML
    public void initialize() {
        registerLink.setVisible(false); // hide register link initially
        loginButton.setOnAction(e -> login());
        registerLink.setOnAction(e -> openRegisterForm());
    }

    private void login() {
        if (!validateInput()) return;

        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        try {
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

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            messageLabel.setText("Database error. Please try again later.");
        }
    }

    private boolean validateInput() {
        if (usernameField.getText().trim().isEmpty() || passwordField.getText().trim().isEmpty()) {
            messageLabel.setText("Please enter username & password.");
            return false;
        }
        return true;
    }

    private void openRegisterForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Register.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Register");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            messageLabel.setText("Can't open registration form.");
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
