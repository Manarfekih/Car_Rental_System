package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.User;
import model.Client;
import service.UserService;
import service.ClientService;

import java.io.IOException;
import java.sql.SQLException;

public class RegisterController {

    @FXML
    private TextField usernameField, emailField;
    @FXML
    private TextField nameField, phoneField, licenseField, addressField;
    @FXML
    private PasswordField passwordField, confirmPasswordField;
    @FXML
    private Button registerButton;
    @FXML
    private Label messageLabel;
    @FXML
    private Button loginLink;

    private final UserService userService = new UserService();
    private final ClientService clientService = new ClientService();

    @FXML
    public void initialize() {
        if (loginLink != null) {
            loginLink.setOnAction(e -> openLogin());
        }
        registerButton.setOnAction(e -> register());
    }

    private void register() {
        // Collect inputs
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String license = licenseField.getText().trim();
        String address = addressField.getText().trim();

        // Validate
        if (!validateInputs(username, email, password, confirmPassword, name, license, address))
            return;

        // Create user
        User user = new User(0, username, email, password, "client");

        int userId = 0;
        try {
            userId = userService.addUser(user);
        } catch (Exception e) {
            e.printStackTrace();
            showError("Database error while creating user.");
            return;
        }

        if (userId <= 0) {
            showError("Error creating user.");
            return;
        }

        user.setUserId(userId);

        // Create client
        Client client = new Client(
                0,
                user,
                name,
                email,
                phone,
                license,
                address
        );

        boolean inserted = false;
        try {
            inserted = clientService.addClient(client);
        } catch (Exception e) {
            e.printStackTrace();
            showError("Database error while creating client.");
            return;
        }

        if (!inserted) {
            showError("Error creating client profile.");
            return;
        }

        showMessage("Registration successful!");
        openMainWindow(user);

        // Close registration window
        Stage stage = (Stage) registerButton.getScene().getWindow();
        stage.close();
    }

    private boolean validateInputs(String username, String email, String password, String confirmPassword,
                                   String name, String license, String address) {

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() ||
                confirmPassword.isEmpty() || name.isEmpty() || license.isEmpty() ||
                address.isEmpty()) {
            showError("Please fill all required fields!");
            return false;
        }

        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match.");
            return false;
        }

        try {
            if (userService.isEmailTaken(email)) {
                showError("Email already registered.");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError("Database error while checking email.");
            return false;
        }

        return true;
    }

    private void showError(String msg) {
        messageLabel.setStyle("-fx-text-fill: red;");
        messageLabel.setText(msg);
    }

    private void showMessage(String msg) {
        messageLabel.setStyle("-fx-text-fill: green;");
        messageLabel.setText(msg);
    }

    private void openLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();

            Stage current = (Stage) loginLink.getScene().getWindow();
            current.close();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Cannot open login form.");
        }
    }

    private void openMainWindow(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Main.fxml"));
            Parent root = loader.load();

            MainController mainController = loader.getController();
            mainController.setLoggedUser(user);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Car Rental System - Welcome " + user.getUsername());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Error opening main window.");
        }
    }
}
