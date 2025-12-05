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
            loginLink.setOnAction(e -> {
                try { openLogin(); }
                catch (Exception ex) { showError("Cannot open login form."); }
            });
        }

        registerButton.setOnAction(e -> {
            try { register(); }
            catch (Exception ex) { showError(ex.getMessage()); }
        });
    }

    private void register() throws Exception {

        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String license = licenseField.getText().trim();
        String address = addressField.getText().trim();

        if (!validateInputs(username, email, password, confirmPassword, name, license, address))
            return;

        User user = new User(0, username, email, password, "client");

        int userId = userService.addUser(user);
        if (userId <= 0) {
            throw new Exception("Error creating user.");
        }

        user.setUserId(userId);

        Client client = new Client(
                0,
                user,
                name,
                email,
                phone,
                license,
                address
        );

        boolean inserted = clientService.addClient(client);

        if (!inserted) {
            throw new Exception("Error creating client profile.");
        }

        showMessage("Registration successful!");
        openMainWindow(user);

        Stage stage = (Stage) registerButton.getScene().getWindow();
        stage.close();
    }

    private boolean validateInputs(String username, String email, String password,
                                   String confirmPassword, String name,
                                   String license, String address) throws Exception {

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

        if (userService.isEmailTaken(email)) {
            showError("Email already registered.");
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

    private void openLogin() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Login");
        stage.show();

        Stage current = (Stage) loginLink.getScene().getWindow();
        current.close();
    }

    private void openMainWindow(User user) throws Exception {

        FXMLLoader loader;
        Parent root;
        Stage stage = new Stage();

        switch (user.getRole()) {
            case "admin":
                loader = new FXMLLoader(getClass().getResource("/view/AdminDashboard.fxml"));
                root = loader.load();
                AdminDashboardController adminController = loader.getController();
                adminController.setLoggedUser(user);
                stage.setTitle("Admin Dashboard - Welcome " + user.getUsername());
                break;

            default:
                loader = new FXMLLoader(getClass().getResource("/view/Main.fxml"));
                root = loader.load();
                MainController mainController = loader.getController();
                mainController.setLoggedUser(user);
                stage.setTitle("Welcome " + user.getUsername());
                break;
        }

        stage.setScene(new Scene(root));
        stage.show();
    }
}
