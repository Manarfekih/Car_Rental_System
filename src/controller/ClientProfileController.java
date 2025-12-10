package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import model.User;
import model.Client;
import dao.ClientDAO;
import service.ClientService;

import java.sql.SQLException;

public class ClientProfileController {

    @FXML
    private Label lblUsername, lblRole;
    @FXML
    private TextField txtName, txtEmail, txtPhone, txtLicense, txtAddress;
    @FXML
    private Button btnUpdate;
    @FXML
    private VBox profileContainer;

    private User loggedUser;
    private Client currentClient;

    private final ClientDAO clientDAO = new ClientDAO();
    private final ClientService clientService = new ClientService();

    public void setLoggedUser(User user) {
        this.loggedUser = user;
        try {
            loadProfile();
        } catch (SQLException | ClassNotFoundException e) {
            showAlert("Database Error", "Failed to load profile.");
        }
    }

    @FXML
    private void initialize() {
        // Add event handler if needed
    }

    private void loadProfile() throws SQLException, ClassNotFoundException {

        lblUsername.setText("Username: " + loggedUser.getUsername());
        lblRole.setText("Role: " + loggedUser.getRole());

        // Get client by user ID
        currentClient = clientDAO.getClientByUserId(loggedUser.getUserId());

        if (currentClient != null) {
            txtName.setText(currentClient.getName());
            txtEmail.setText(currentClient.getEmail());
            txtPhone.setText(currentClient.getPhone() != null ? currentClient.getPhone() : "");
            txtLicense.setText(currentClient.getLicenseNumber());
            txtAddress.setText(currentClient.getAddress() != null ? currentClient.getAddress() : "");
        } else {
            showAlert("Error", "Client profile not found!");
        }
    }

    @FXML
    private void handleUpdate() {
        try {
            updateProfile();
        } catch (SQLException | ClassNotFoundException e) {
            showAlert("Database Error", "Failed to update profile.");
        } catch (IllegalArgumentException e) {
            showAlert("Validation Error", e.getMessage());
        }
    }

    private void updateProfile() throws SQLException, ClassNotFoundException {

        if (currentClient == null) {
            showAlert("Error", "No client profile found!");
            return;
        }

        // Validate inputs
        if (txtName.getText().isEmpty() || txtEmail.getText().isEmpty() ||
            txtLicense.getText().isEmpty()) {
            throw new IllegalArgumentException("Name, Email and License Number are required fields.");
        }

        // Update client object
        currentClient.setName(txtName.getText().trim());
        currentClient.setEmail(txtEmail.getText().trim());
        currentClient.setPhone(txtPhone.getText().trim());
        currentClient.setLicenseNumber(txtLicense.getText().trim());
        currentClient.setAddress(txtAddress.getText().trim());

        // Update in database
        boolean updated = clientDAO.updateClient(currentClient);

        if (updated) {
            showAlert("Success", "Profile updated successfully!");
        } else {
            showAlert("Error", "Failed to update profile.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
