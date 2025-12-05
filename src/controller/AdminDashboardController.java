package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.User;
import service.*;

import java.io.IOException;
import java.sql.SQLException;

public class AdminDashboardController {

    @FXML private StackPane contentArea;
    @FXML private Button btnCars;
    @FXML private Button btnClients;
    @FXML private Button btnRentals;
    @FXML private Button btnStatistics;
    @FXML private Button btnLogout;
    @FXML private Button btnBack;
    @FXML private Label lblWelcome;
    @FXML private VBox sidebar;
    @FXML private Button btnEmployees;


    // Track current view
    private String currentView = "dashboard";

    // Services
    private final CarService carService = new CarService();
    private final ClientService clientService = new ClientService();
    private final RentalService rentalService = new RentalService();
    private final StatisticsService statisticsService = new StatisticsService();
    private final UserService userService = new UserService();

    private User loggedUser;

    public void setLoggedUser(User user) throws Exception {
        this.loggedUser = user;
        updateUI();
        loadDashboard();
    }

    @FXML
    public void initialize() {
        setupButtonStyles();
        
        // Initially hide back button (only show when not on dashboard)
        btnBack.setVisible(false);

        btnCars.setOnAction(e -> {
            try { 
                loadCarsManagement();
                btnBack.setVisible(true); // Show back button
            } catch (Exception ex) { 
                showError("Unable to load Cars", ex.getMessage()); 
            }
        });

        btnClients.setOnAction(e -> {
            try { 
                loadClientsManagement();
                btnBack.setVisible(true); // Show back button
            } catch (Exception ex) { 
                showError("Unable to load Clients", ex.getMessage()); 
            }
        });

        btnRentals.setOnAction(e -> {
            try { 
                loadRentalsManagement();
                btnBack.setVisible(true); // Show back button
            } catch (Exception ex) { 
                showError("Unable to load Rentals", ex.getMessage()); 
            }
        });

        btnStatistics.setOnAction(e -> {
            try { 
                loadStatistics();
                btnBack.setVisible(true); // Show back button
            } catch (Exception ex) { 
                showError("Unable to load Statistics", ex.getMessage()); 
            }
        });
        
        btnBack.setOnAction(e -> {
            try { 
                loadDashboard();
                btnBack.setVisible(false); // Hide back button
            } catch (Exception ex) { 
                showError("Error returning to dashboard", ex.getMessage()); 
            }
        });

        btnLogout.setOnAction(e -> {
            try { logout(); }
            catch (Exception ex) { showError("Logout Error", ex.getMessage()); }
        });
        
        btnEmployees.setOnAction(e -> {
            try {
                loadEmployeesManagement();
                btnBack.setVisible(true);
            } catch (Exception ex) {
                showError("Unable to load Employees", ex.getMessage());
            }
        });

    }

    private void setupButtonStyles() {
        String buttonStyle = "-fx-background-color: #34495e; -fx-text-fill: white; "
                           + "-fx-font-weight: bold; -fx-min-width: 150; "
                           + "-fx-min-height: 40; -fx-border-radius: 5; "
                           + "-fx-background-radius: 5;";

        String backButtonStyle = "-fx-background-color: #f39c12; -fx-text-fill: white; "
                               + "-fx-font-weight: bold; -fx-min-width: 150; "
                               + "-fx-min-height: 40; -fx-border-radius: 5; "
                               + "-fx-background-radius: 5;";

        btnCars.setStyle(buttonStyle);
        btnClients.setStyle(buttonStyle);
        btnRentals.setStyle(buttonStyle);
        btnStatistics.setStyle(buttonStyle);
        btnBack.setStyle(backButtonStyle);
        btnLogout.setStyle(buttonStyle + "-fx-background-color: #e74c3c;");
    }

    private void updateUI() {
        lblWelcome.setText("Welcome, " + loggedUser.getUsername());
    }

    private void loadDashboard() throws SQLException, ClassNotFoundException {
        currentView = "dashboard";

        VBox dashboard = new VBox(20);
        dashboard.setStyle("-fx-padding: 20; -fx-alignment: center;");

        int totalCars = carService.getTotalCarsCount();
        int totalClients = clientService.getTotalClientsCount();
        int activeRentals = rentalService.getActiveRentalsCount();
        double totalRevenue = statisticsService.getTotalRevenue();

        Label statsLabel = new Label("System Overview");
        statsLabel.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        VBox statsBox = new VBox(10);
        statsBox.getChildren().addAll(
                createStatCard("Total Cars", String.valueOf(totalCars), "#3498db"),
                createStatCard("Total Clients", String.valueOf(totalClients), "#2ecc71"),
                createStatCard("Active Rentals", String.valueOf(activeRentals), "#e74c3c"),
                createStatCard("Total Revenue", String.format("$%.2f", totalRevenue), "#f39c12")
        );

        dashboard.getChildren().addAll(statsLabel, statsBox);
        contentArea.getChildren().setAll(dashboard);
    }


    private void loadEmployeesManagement() throws IOException {
        currentView = "employees";
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/EmployeeManagement.fxml"));
        Parent view = loader.load();
        contentArea.getChildren().setAll(view);
    }


    private VBox createStatCard(String title, String value, String color) {
        VBox card = new VBox(5);
        card.setStyle(String.format(
                "-fx-background-color: white; -fx-padding: 15; "
                + "-fx-background-radius: 10; -fx-border-color: %s; "
                + "-fx-border-width: 2; -fx-border-radius: 10; "
                + "-fx-alignment: center;", color));

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

        Label valueLabel = new Label(value);
        valueLabel.setStyle(String.format("-fx-font-size: 24; -fx-text-fill: %s; -fx-font-weight: bold;", color));

        card.getChildren().addAll(titleLabel, valueLabel);
        return card;
    }

    // -------------------------
    //  VIEW LOADING METHODS
    // -------------------------

    private void loadCarsManagement() throws IOException {
        currentView = "cars";
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/cars.fxml"));
        Parent view = loader.load();
        contentArea.getChildren().setAll(view);
    }

    private void loadClientsManagement() throws IOException {
        currentView = "clients";
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/clients.fxml"));
        Parent view = loader.load();
        contentArea.getChildren().setAll(view);
    }

    private void loadRentalsManagement() throws IOException {
        currentView = "rentals";
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/RentalManagement.fxml"));
        Parent view = loader.load();
        contentArea.getChildren().setAll(view);
    }

    private void loadStatistics() throws IOException {
        currentView = "statistics";
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/statistics.fxml"));
        Parent view = loader.load();
        contentArea.getChildren().setAll(view);
    }

    private void logout() throws IOException {
        Stage currentStage = (Stage) btnLogout.getScene().getWindow();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
        Parent loginRoot = loader.load();

        Stage loginStage = new Stage();
        loginStage.setScene(new Scene(loginRoot, 600, 400));
        loginStage.setTitle("Car Rental System - Login");
        loginStage.show();

        currentStage.close();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}