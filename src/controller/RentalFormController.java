package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Rental;
import model.Client;
import model.Car;
import service.RentalService;
import service.ClientService;
import service.CarService;

import java.time.LocalDate;
import java.util.List;

public class RentalFormController {

    @FXML private TextField clientField;  // Changed from ComboBox
    @FXML private TextField carField;     // Changed from ComboBox
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private TextField priceField;
    @FXML private ComboBox<String> statusCombo;
    @FXML private Button saveBtn;
    @FXML private Button cancelBtn;
    @FXML private Label statusLabel;

    private final RentalService rentalService = new RentalService();
    private final ClientService clientService = new ClientService();
    private final CarService carService = new CarService();

    private boolean editMode = false;
    private Rental editingRental = null;
    private boolean saved = false;

    @FXML
    public void initialize() {
        // Initialize status combo
        statusCombo.getItems().addAll("active", "completed", "overdue");
        statusCombo.setValue("active");
        
        statusLabel.setText("");
        
        saveBtn.setOnAction(e -> {
            try {
                save();
            } catch (Exception ex) {
                showError("Save Error", ex.getMessage());
            }
        });
        
        cancelBtn.setOnAction(e -> close());
    }

    public void setEditMode(Rental rental) {
        this.editMode = true;
        this.editingRental = rental;
        populateFields(rental);
    }

    private void populateFields(Rental r) {
        clientField.setText(r.getClient() != null ? r.getClient().getName() : "");
        carField.setText(r.getCar() != null ? 
            r.getCar().getBrand() + " " + r.getCar().getModel() : "");
        startDatePicker.setValue(r.getStartDate());
        endDatePicker.setValue(r.getEndDate());
        priceField.setText(String.valueOf(r.getTotalPrice()));
        statusCombo.setValue(r.getStatus() != null ? r.getStatus() : "active");
    }

    public boolean isSaved() { 
        return saved; 
    }

    public void save() throws Exception {
        validateAndSave();
    }

    private void validateAndSave() throws Exception {
        statusLabel.setText("");
        
        debugAllCars();
        
        // Get values from form
        String clientName = clientField.getText().trim();
        String carInfo = carField.getText().trim();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        String priceStr = priceField.getText().trim();
        String status = statusCombo.getValue();

        // Validation
        if (clientName.isEmpty() || carInfo.isEmpty() || startDate == null || 
            endDate == null || priceStr.isEmpty()) {
            statusLabel.setText("All fields are required.");
            return;
        }

        if (startDate.isAfter(endDate)) {
            statusLabel.setText("Start date must be before end date.");
            return;
        }

        try {
            double price = Double.parseDouble(priceStr);
            
            if (price <= 0) {
                statusLabel.setText("Price must be greater than 0.");
                return;
            }

            // Find client
            Client client = findClientByName(clientName);
            if (client == null) {
                statusLabel.setText("Client not found: " + clientName);
                return;
            }

            // Find car
            Car car = findCarByInfo(carInfo);
            if (car == null) {
                statusLabel.setText("Car not found: " + carInfo);
                return;
            }

            if (!editMode) {
                // Add new rental
                Rental rental = new Rental(0, client, car, startDate, endDate, price, status);
                boolean success = rentalService.addRental(rental);
                if (success) {
                    saved = true;
                    close();
                } else {
                    statusLabel.setText("Failed to create rental.");
                }
            } else {
                // Edit existing rental
                editingRental.setClient(client);
                editingRental.setCar(car);
                editingRental.setStartDate(startDate);
                editingRental.setEndDate(endDate);
                editingRental.setTotalPrice(price);
                editingRental.setStatus(status);
                
                boolean success = rentalService.updateRental(editingRental);
                if (success) {
                    saved = true;
                    close();
                } else {
                    statusLabel.setText("Failed to update rental.");
                }
            }
            
        } catch (NumberFormatException e) {
            statusLabel.setText("Price must be a valid number.");
            return;
        }
    }

    private Client findClientByName(String name) throws Exception {
        List<Client> clients = clientService.getAllClients();
        for (Client c : clients) {
            if (c.getName().equalsIgnoreCase(name)) {
                return c;
            }
        }
        return null;
    }

    private Car findCarByInfo(String carInfo) throws Exception {
        List<Car> cars = carService.getAllCars();
        
        System.out.println("DEBUG: Searching for car: '" + carInfo + "'");
        System.out.println("DEBUG: Total cars in database: " + cars.size());
        
        // Trim and normalize the search input
        String searchTerm = carInfo.trim().toLowerCase();
        
        // Try different search patterns
        for (Car c : cars) {
            String brand = c.getBrand() != null ? c.getBrand().trim().toLowerCase() : "";
            String model = c.getModel() != null ? c.getModel().trim().toLowerCase() : "";
            
            // Create different possible display formats
            String fullName = brand + " " + model;
            String reverseName = model + " " + brand;
            String brandOnly = brand;
            String modelOnly = model;
            
            System.out.println("DEBUG: Checking car - Brand: '" + brand + "', Model: '" + model + "'");
            
            // Try multiple matching strategies
            if (searchTerm.equals(fullName) || 
                searchTerm.equals(reverseName) ||
                searchTerm.equals(brandOnly) ||
                searchTerm.equals(modelOnly) ||
                fullName.contains(searchTerm) ||
                brand.equals(searchTerm) ||
                model.equals(searchTerm)) {
                
                System.out.println("DEBUG: Found match - Car ID: " + c.getCarId() + 
                                 ", Brand: " + c.getBrand() + ", Model: " + c.getModel());
                return c;
            }
        }
        
        System.out.println("DEBUG: No car found for: " + carInfo);
        return null;
    }
    private void debugAllCars() throws Exception {
        List<Car> cars = carService.getAllCars();
        System.out.println("=== ALL CARS IN DATABASE ===");
        for (Car c : cars) {
            System.out.println("ID: " + c.getCarId() + 
                             " | Brand: '" + c.getBrand() + "'" + 
                             " | Model: '" + c.getModel() + "'" +
                             " | Status: " + c.getStatus());
        }
        System.out.println("Total cars: " + cars.size());
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