package controller;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import model.User;
import model.Client;
import model.Rental;
import dao.ClientDAO;
import dao.RentalDAO;

import java.sql.SQLException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;

public class ClientMyRentalsController {

    @FXML
    private VBox rentalsListContainer;

    private User loggedUser;
    private Client currentClient;

    private final ClientDAO clientDAO = new ClientDAO();
    private final RentalDAO rentalDAO = new RentalDAO();

    // Entry point from Dashboard → we keep try/catch ONLY here
    public void setLoggedUser(User user) {
        this.loggedUser = user;

        try {
            loadCurrentClient();
            loadMyRentals();
        } catch (SQLException | ClassNotFoundException e) {
            showAlert("Database Error", e.getMessage());
            e.printStackTrace();
        }
    }

    
    private void loadCurrentClient() throws SQLException, ClassNotFoundException {
        currentClient = clientDAO.getClientByUserId(loggedUser.getUserId());

        if (currentClient == null) {
            throw new SQLException("Client not found for user ID: " + loggedUser.getUserId());
        }
    }

    private void loadMyRentals() throws SQLException, ClassNotFoundException {
        rentalsListContainer.getChildren().clear();

        if (currentClient == null) {
            rentalsListContainer.getChildren().add(new Label("Client information not found."));
            return;
        }

        List<Rental> rentals = rentalDAO.getAllRentals();

        // load rentals+ensure no duplicates
        Set<Integer> seen = new java.util.HashSet<>();
        rentals.removeIf(r -> !seen.add(r.getRentalId()));

        boolean hasRentals = false;

        for (Rental rental : rentals) {
            if (rental.getClient().getClientId() == currentClient.getClientId()) {
                hasRentals = true;

                long rentalDays = ChronoUnit.DAYS.between(
                        rental.getStartDate(), rental.getEndDate()
                );

                Label rentalLabel = new Label(
                        String.format(
                                "🚗 Car: %s %s\n" +
                                        "📅 Period: %s to %s (%d days)\n" +
                                        "💰 Price/Day: $%.2f | Total: $%.2f\n" +
                                        "📊 Status: %s",
                                rental.getCar().getBrand(), rental.getCar().getModel(),
                                rental.getStartDate(), rental.getEndDate(), rentalDays,
                                rental.getCar().getPricePerDay(), rental.getTotalPrice(),
                                rental.getStatus().toUpperCase()
                        )
                );

                rentalLabel.setStyle("-fx-padding: 15; -fx-border-color: #ddd; -fx-background-color: white;");

                VBox rentalBox = new VBox(rentalLabel);
                rentalBox.setSpacing(10);
                rentalBox.setStyle("-fx-padding: 10; -fx-border-color: #ccc;");

                rentalsListContainer.getChildren().add(rentalBox);
            }
        }

        if (!hasRentals) {
            rentalsListContainer.getChildren().add(
                    new Label("You have no rental history.")
            );
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
