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
        welcomeLabel.setText("Welcome, " + loggedUser.getUsername());
    }

    /**
     * Show/hide buttons based on role
     */
    private void configureUIByRole() {
        String role = loggedUser.getRole();
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
                manageUsersButton.setVisible(false);
                manageCarsButton.setVisible(false);
                manageRentalsButton.setVisible(false);
        }
    }

    /**
     * Open User Management (Admin only)
     */
    @FXML
    private void openUserManagement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UserManagement.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("User Management");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Open Car Management (Admin & Employee)
     */
    @FXML
    private void openCarManagement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CarManagement.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Car Management");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Open Rental Management (Employee & Client)
     */
    @FXML
    private void openRentalManagement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/RentalManagement.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Rental Management");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Logout and return to Login
     */
    @FXML
    private void logout() {
        try {
            // Load the login FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
            Parent root = loader.load();

            // Get the current stage (the main window)
            Stage stage = (Stage) logoutButton.getScene().getWindow();

            // Set the scene to login
            stage.setScene(new Scene(root));
            stage.setTitle("Login");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
}
