package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Client;
import service.ClientService;

import java.sql.SQLException;

public class ClientOp {

    @FXML private Label dialogTitle;
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField licenseField;
    @FXML private TextField addressField;
    @FXML private Button btnSave;
    @FXML private Button btnCancel;

    private ClientService clientService = new ClientService();
    private Client client; 
    private boolean saved = false;

    public void setClient(Client client) {
        this.client = client;
        if (client != null) { 
            dialogTitle.setText("Edit Client");
            nameField.setText(client.getName());
            emailField.setText(client.getEmail());
            phoneField.setText(client.getPhone());
            licenseField.setText(client.getLicenseNumber());
            addressField.setText(client.getAddress());
        } else {
            dialogTitle.setText("Add New Client");
        }
    }

    public boolean isSaved() {
        return saved;
    }

    public Client getClient() {
        return client;
    }

    @FXML
    public void initialize() {
        btnSave.setOnAction(e -> saveClient());
        btnCancel.setOnAction(e -> closeDialog());
    }

    private void saveClient() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String license = licenseField.getText().trim();
        String address = addressField.getText().trim();

        if (name.isEmpty() || email.isEmpty() || license.isEmpty()) {
            showAlert("Validation Error", "Name, Email and License are required.");
            return;
        }

        try {
            if (client == null) { 
                // ✔ CORRECTED — proper constructor
                client = new Client(
                        0,      // clientId (auto)
                        null,   // user (no need for now)
                        name,
                        email,
                        phone,
                        license,
                        address
                );
            } else { 
                // Update existing client
                client.setName(name);
                client.setEmail(email);
                client.setPhone(phone);
                client.setLicenseNumber(license);
                client.setAddress(address);
            }

            if (client.getClientId() == 0) {
                clientService.addClient(client);
            } else {
                clientService.updateClient(client);
            }

            saved = true;
            closeDialog();

        } catch (SQLException | ClassNotFoundException e) {
            showAlert("Database Error", e.getMessage());
        }
    }

    private void closeDialog() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
