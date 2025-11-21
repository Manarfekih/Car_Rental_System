package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.User;
import service.UserService;
import model.Client;
import service.ClientService;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class RegisterController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private TextField emailField;

    @FXML
    private ComboBox<String> roleComboBox;

    @FXML
    private Button registerButton;

    @FXML
    private Label messageLabel;

    // Valid roles for ComboBox
    private final List<String> validRoles = Arrays.asList("admin", "employee", "client");

    @FXML
    public void initialize() {
        roleComboBox.getItems().addAll(validRoles);
        roleComboBox.setValue("client"); // default role

        registerButton.setOnAction(e -> register());
    }

    private void register() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();
        String email = emailField.getText().trim();
        String role = roleComboBox.getValue();

        // Basic validation
        if (username.isBlank() || email.isBlank() || password.isBlank() || role == null || role.isBlank()) {
            messageLabel.setText("Please fill all fields!");
            return;
        }

        // Validate role
        if (!validRoles.contains(role)) {
            messageLabel.setText("Invalid role selected!");
            return;
        }

        // Match passwords
        if (!password.equals(confirmPassword)) {
            messageLabel.setText("Passwords do not match.");
            return;
        }

        UserService userService = new UserService();

        // Prevent duplicate email for client role
        if ("client".equals(role) && userService.isEmailTaken(email)) {
            messageLabel.setText("Email already registered. Please log in.");
            return;
        }

        // Create new user
        User newUser = new User(0, username, email, password, role);

        int userId = userService.addUser(newUser);

        System.out.println("Inserted new user_id = " + userId);

        if (userId <= 0) {
            System.out.println("ERROR: user not inserted!");
            return;
        }

        // Add client entry only if role = client
        if ("client".equals(role)) {
            ClientService clientService = new ClientService();
            Client client = new Client(0, userId, username, email, "", "", "");

            boolean clientAdded = clientService.addClient(client);

            if (!clientAdded) {
                messageLabel.setText("Could not create client profile.");
                return;
            }
        }

        // Success
        messageLabel.setText("Registration successful! Logging you in...");

        // Open main window
        User loggedUser = userService.getUserById(userId);
        openMainWindow(loggedUser);

        // Close registration window
        Stage stage = (Stage) registerButton.getScene().getWindow();
        stage.close();
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
