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

    private final UserService userService = new UserService();
    private final ClientService clientService = new ClientService();

    @FXML
    public void initialize() {
        registerButton.setOnAction(e -> register());
    }

    private void register() {
        // Read inputs
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String license = licenseField.getText().trim();
        String address = addressField.getText().trim();

        // Validate required fields
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() ||
            name.isEmpty() || license.isEmpty() || address.isEmpty()) {
            messageLabel.setText("Please fill all required fields!");
            return;
        }

        // Password match
        if (!password.equals(confirmPassword)) {
            messageLabel.setText("Passwords do not match.");
            return;
        }

        // Check duplicate email
        if (userService.isEmailTaken(email)) {
            messageLabel.setText("Email already registered. Please log in.");
            return;
        }

        // Create user (role fixed to 'client')
        User user;
        try {
            user = new User(0, username, email, password, "client");
        } catch (IllegalArgumentException e) {
            messageLabel.setText("Invalid input: " + e.getMessage());
            return;
        }

        int userId = userService.addUser(user);
        if (userId <= 0) {
            messageLabel.setText("Error creating user. Try again.");
            return;
        }

        // Create client linked to user
        Client client = new Client(0, userId, name, email, phone, license, address);
        if (!clientService.addClient(client)) {
            messageLabel.setText("Error creating client profile.");
            return;
        }

        // Success
        messageLabel.setText("Registration successful!");

        // Open main window
        User loggedUser = userService.getUserById(userId);
        if (loggedUser != null) {
            openMainWindow(loggedUser);
            // Close registration window
            Stage stage = (Stage) registerButton.getScene().getWindow();
            stage.close();
        } else {
            messageLabel.setText("Could not retrieve user after registration.");
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
            messageLabel.setText("Error opening main window.");
        }
    }
}
