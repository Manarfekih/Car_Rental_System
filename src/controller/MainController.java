package controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.User;

public class MainController {

    private User loggedUser;

    // UI elements
    @FXML
    private Label welcomeLabel;

    @FXML
    private Button manageUsersButton; // Admin only

    @FXML
    private Button manageCarsButton;  // Admin & Employee

    @FXML
    private Button manageRentalsButton; // Employee & Client

    @FXML
    private Button logoutButton;

    /**
     * Set the logged-in user and initialize the UI
     */
    public void setLoggedUser(User user) {
        this.loggedUser = user;
        initUserData();
        configureUIByRole();
    }

    /**
     * Display username and role
     */
    private void initUserData() {
        welcomeLabel.setText("Welcome, " + loggedUser.getUsername() + " (" + loggedUser.getRole() + ")");
    }

    /**
     * Show/hide buttons based on role
     */
    private void configureUIByRole() {
        String role = loggedUser.getRole().toLowerCase();
        
        switch (role) {
            case "admin":
                manageUsersButton.setVisible(true);
                manageCarsButton.setVisible(true);
                manageRentalsButton.setVisible(true);
                break;
            case "employee":
                manageUsersButton.setVisible(false);
                manageCarsButton.setVisible(true);
                manageRentalsButton.setVisible(true);
                break;
            case "client":
                manageUsersButton.setVisible(false);
                manageCarsButton.setVisible(false);
                manageRentalsButton.setVisible(true);
                break;
            default:
                // Hide all for unknown roles
                manageUsersButton.setVisible(false);
                manageCarsButton.setVisible(false);
                manageRentalsButton.setVisible(false);
        }
    }

    /**
     * Open User Management (Admin only)
     * @throws Exception 
     */
    @FXML
    private void openUserManagement() throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AdminDashboard.fxml"));
            Parent root = loader.load();
            
            // Pass the user to AdminDashboardController
            AdminDashboardController controller = loader.getController();
            controller.setLoggedUser(loggedUser);
            
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Admin Dashboard");
            stage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Cannot open Admin Dashboard: " + e.getMessage());
        }
    }

    /**
     * Open Car Management (Admin & Employee)
     */
    @FXML
    private void openCarManagement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/cars.fxml"));
            Parent root = loader.load();
            
            // Pass user to CarManagementController if needed
            CarManagementController controller = loader.getController();
            // If CarManagementController has setLoggedUser method
            // controller.setLoggedUser(loggedUser);
            
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Car Management");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Cannot open Car Management: " + e.getMessage());
        }
    }

    /**
     * Open Rental Management (Employee & Client)
     */
    @FXML
    private void openRentalManagement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/rentals.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Rental Management");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Cannot open Rental Management: " + e.getMessage());
        }
    }

    /**
     * Logout and return to Login
     */
    @FXML
    private void logout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Cannot logout: " + e.getMessage());
        }
    }
    
    private void showAlert(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
            javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}