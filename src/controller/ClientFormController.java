package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Client;
import model.User;
import service.ClientService;
import service.UserService;

public class ClientFormController {

    @FXML private Label titleLabel;
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField licenseField;
    @FXML private TextField addressField;
    @FXML private Button saveBtn;
    @FXML private Button cancelBtn;
    @FXML private Label statusLabel;

    private final ClientService clientService = new ClientService();
    private final UserService userService = new UserService();

    private boolean editMode = false;
    private Client editingClient = null;
    private boolean saved = false;

    @FXML
    public void initialize() throws Exception {
        statusLabel.setText("");
        saveBtn.setOnAction(e -> {
            try {
                save();
            } catch (Exception ex) {
                // Convert to RuntimeException to avoid changing method signature
                throw new RuntimeException("Error saving client: " + ex.getMessage(), ex);
            }
        });
        cancelBtn.setOnAction(e -> close());
    }

    public void setEditMode(Client client) {
        this.editMode = true;
        this.editingClient = client;
        titleLabel.setText("Edit Client");
        populateFields(client);
    }

    private void populateFields(Client c) {
        nameField.setText(c.getName() != null ? c.getName() : "");
        emailField.setText(c.getEmail() != null ? c.getEmail() : "");
        phoneField.setText(c.getPhone() != null ? c.getPhone() : "");
        licenseField.setText(c.getLicenseNumber() != null ? c.getLicenseNumber() : "");
        addressField.setText(c.getAddress() != null ? c.getAddress() : "");
    }

    public boolean isSaved() { 
        return saved; 
    }

    // Method now throws Exception
    public void save() throws Exception {
        validateAndSave();
    }

    // Private helper that does the actual work
    private void validateAndSave() throws Exception {
        statusLabel.setText("");
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String license = licenseField.getText().trim();
        String address = addressField.getText().trim();

        // Validation that doesn't throw
        if (name.isEmpty() || email.isEmpty() || license.isEmpty()) {
            statusLabel.setText("Name, Email and License are required.");
            return; // This is validation, not an exception
        }

        if (!editMode) {
            addNewClient(name, email, phone, license, address);
        } else {
            editExistingClient(name, email, phone, license, address);
        }
    }

    private void addNewClient(String name, String email, String phone, String license, String address) 
            throws Exception {
        
        if (userService.isEmailTaken(email)) {
            statusLabel.setText("Email already used.");
            return; // Validation, not exception
        }

        String username = email.contains("@") ? email.substring(0, email.indexOf("@")) : email;
        String password = "default123";
        
        // Create User
        User user = new User(0, username, email, password, "client");
        int userId = userService.addUser(user);
        user.setUserId(userId);

        // Create Client
        Client client = new Client(0, user, name, email, phone, license, address);
        boolean clientAdded = clientService.addClient(client);
        
        if (clientAdded) {
            saved = true;
            close();
        } else {
            throw new Exception("Failed to add client to database.");
        }
    }

    private void editExistingClient(String name, String email, String phone, String license, String address) 
            throws Exception {
        
        // Check if email is being changed
        boolean emailChanged = !email.equals(editingClient.getEmail());
        
        if (emailChanged) {
            // Check if new email is already taken by another user
            if (userService.isEmailTaken(email)) {
                statusLabel.setText("Email already used by another user.");
                return;
            }
            
            // Update User's email
            User user = editingClient.getUser();
            if (user != null) {
                String oldEmail = user.getEmail();
                user.setEmail(email);
                
                // Update user in database
                boolean userUpdated = userService.updateUser(user);
                if (!userUpdated) {
                    // Rollback email change
                    user.setEmail(oldEmail);
                    throw new Exception("Failed to update user email.");
                }
            }
        }
        // Update Client
        editingClient.setName(name);
        editingClient.setEmail(email);
        editingClient.setPhone(phone);
        editingClient.setLicenseNumber(license);
        editingClient.setAddress(address);

        boolean updated = clientService.updateClient(editingClient);
        
        if (updated) {
            saved = true;
            close();
        } else {
            throw new Exception("Failed to update client in database.");
        }
    }

    private void close() {
        Stage s = (Stage) cancelBtn.getScene().getWindow();
        s.close();
    }

    private void showError(String title, String message) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(message);
        a.showAndWait();
    }
}