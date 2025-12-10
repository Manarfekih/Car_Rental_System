package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Label messageLabel;
    @FXML private Button registerLink;

    private final UserService userService = new UserService();

    @FXML
    public void initialize() {
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
                messageLabel.setText("Login successful!");
                openMainWindow(user);
            } else {
                messageLabel.setText("User not found.");
                registerLink.setVisible(true);
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            messageLabel.setText("Database error.");
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
            Parent root = FXMLLoader.load(getClass().getResource("/view/Register.fxml"));
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
            FXMLLoader loader;
            Parent root;
            Stage stage = new Stage();

            String role = user.getRole().toLowerCase();

            switch (role) {
                case "admin":
                    loader = new FXMLLoader(getClass().getResource("/view/AdminDashboard.fxml"));
                    root = loader.load();
                    AdminDashboardController adminController = loader.getController();
                    adminController.setLoggedUser(user);
                    stage.setTitle("Admin Dashboard");
                    break;

                case "client":
                    loader = new FXMLLoader(getClass().getResource("/view/ClientDashboard.fxml"));
                    root = loader.load();
                    ClientDashboardController clientController = loader.getController();
                    clientController.setLoggedUser(user);
                    stage.setTitle("Client Dashboard");
                    break;

                case "employee":
                    loader = new FXMLLoader(getClass().getResource("/view/EmployeeDashboard.fxml"));
                    root = loader.load();
                    stage.setTitle("Employee Dashboard");
                    break;

                default:
                    messageLabel.setText("Unknown role: " + user.getRole());
                    return;
            }

            stage.setScene(new Scene(root, 1000, 700));
            stage.show();

            // Close login window
            Stage loginStage = (Stage) usernameField.getScene().getWindow();
            loginStage.close();

        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Error: " + e.getMessage());

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Load Error");
            alert.setContentText("Cannot load window: " + e.getMessage());
            alert.showAndWait();
        }
    }


   
}