package controller;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Alert;
import model.User;
import model.Client;
import model.Car;
import model.Rental;
import dao.CarDAO;
import dao.RentalDAO;
import dao.ClientDAO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class ClientAvailableCarsController {

    @FXML
    private VBox carsListContainer;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;

    private User loggedUser;
    private Client currentClient;

    private final CarDAO carDAO = new CarDAO();
    private final RentalDAO rentalDAO = new RentalDAO();
    private final ClientDAO clientDAO = new ClientDAO();

    public void setLoggedUser(User user) {
        this.loggedUser = user;

        try {
            loadCurrentClient();
            initializeDatePickers();
            loadAvailableCars();
        } catch (Exception e) {
            showAlert("Error", "Failed to load client or car data.");
            e.printStackTrace();
        }
    }

    private void loadCurrentClient() throws SQLException, ClassNotFoundException {
        currentClient = clientDAO.getClientByUserId(loggedUser.getUserId());
        if (currentClient == null) {
            throw new SQLException("Client not found for user ID");
        }
    }

    private void initializeDatePickers() {
        startDatePicker.setValue(LocalDate.now());
        endDatePicker.setValue(LocalDate.now().plusDays(1));
    }

    private void loadAvailableCars() throws SQLException, ClassNotFoundException {
        carsListContainer.getChildren().clear();

        List<Car> cars = carDAO.getAvailableCars();

        // sort by brand name
        cars.sort((c1, c2) -> c1.getBrand().compareToIgnoreCase(c2.getBrand()));

        if (cars.isEmpty()) {
            carsListContainer.getChildren().add(new Label("No available cars at the moment."));
            return;
        }

        for (Car car : cars) {

            Label label = new Label(
                String.format("%s %s (%d) | Category: %s | Price/Day: $%.2f",
                        car.getBrand(), car.getModel(), car.getYear(),
                        car.getCategory(), car.getPricePerDay())
            );
            label.setStyle("-fx-padding: 10; -fx-border-color: #ddd; -fx-background-color: white; -fx-border-radius: 5;");

            Button btnRent = new Button("Rent Now");
            btnRent.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold;");
            btnRent.setOnAction(e -> {
                try {
                    handleRentCar(car);
                } catch (Exception ex) {
                    showAlert("Error", "Failed to process rental.");
                    ex.printStackTrace();
                }
            });

            VBox carBox = new VBox(label, btnRent);
            carBox.setSpacing(5);
            carBox.setStyle("-fx-padding: 10; -fx-border-color: #ccc; -fx-border-radius: 5; -fx-background-color: #f9f9f9;");
            carsListContainer.getChildren().add(carBox);
        }
    }

    private void handleRentCar(Car car) throws SQLException, ClassNotFoundException {
        if (currentClient == null) {
            showAlert("Error", "Client information not found!");
            return;
        }

        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        if (startDate == null || endDate == null) {
            showAlert("Validation Error", "Please select both start and end dates.");
            return;
        }
        if (startDate.isBefore(LocalDate.now())) {
            showAlert("Validation Error", "Start date cannot be in the past.");
            return;
        }
        if (endDate.isBefore(startDate)) {
            showAlert("Validation Error", "End date must be after start date.");
            return;
        }

        long rentalDays = ChronoUnit.DAYS.between(startDate, endDate);
        if (rentalDays < 1) {
            showAlert("Validation Error", "Rental must be at least 1 day.");
            return;
        }

        double totalPrice = car.getPricePerDay() * rentalDays;

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Rental");
        confirmAlert.setHeaderText("Rental Confirmation");
        confirmAlert.setContentText(String.format(
                "Car: %s %s\n" +
                "Rental Period: %s to %s (%d days)\n" +
                "Price per day: $%.2f\n" +
                "Total Price: $%.2f\n\n" +
                "Do you want to proceed?",
                car.getBrand(), car.getModel(),
                startDate, endDate, rentalDays,
                car.getPricePerDay(), totalPrice
        ));

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                try {
                    rentCar(car, startDate, endDate, totalPrice);
                } catch (Exception ex) {
                    showAlert("Error", "Failed to finalize rental.");
                    ex.printStackTrace();
                }
            }
        });
    }

    private void rentCar(Car car, LocalDate startDate, LocalDate endDate, double totalPrice)
            throws SQLException, ClassNotFoundException {

        Rental rental = new Rental(
                0,
                currentClient,
                car,
                startDate,
                endDate,
                totalPrice,
                "active"
        );

        boolean rentalCreated = rentalDAO.createRental(rental);

        if (!rentalCreated) {
            showAlert("Error", "Failed to create rental.");
            return;
        }

        car.setStatus("rented");
        carDAO.updateCar(car);

        showAlert("Success", "Car rented successfully!");

        loadAvailableCars();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
