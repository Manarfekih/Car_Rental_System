package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Car;
import service.CarService;

public class CarFormController {

    @FXML private Label titleLabel;
    @FXML private TextField brandField;
    @FXML private TextField modelField;
    @FXML private TextField yearField;
    @FXML private TextField categoryField;
    @FXML private TextField priceField;
    @FXML private ComboBox<String> statusCombo;
    @FXML private Button saveBtn;
    @FXML private Button cancelBtn;
    @FXML private Label statusLabel;

    private final CarService carService = new CarService();

    private boolean editMode = false;
    private Car editingCar = null;
    private boolean saved = false;

    @FXML
    public void initialize() {
        // Initialize status combo box
        statusCombo.getItems().addAll("available", "rented", "maintenance");
        statusCombo.setValue("available");
        
        statusLabel.setText("");
        saveBtn.setOnAction(e -> {
            try {
                save();
            } catch (Exception ex) {
                // Convert to RuntimeException to avoid changing method signature
                throw new RuntimeException("Error saving car: " + ex.getMessage(), ex);
            }
        });
        cancelBtn.setOnAction(e -> close());
    }

    public void setEditMode(Car car) {
        this.editMode = true;
        this.editingCar = car;
        titleLabel.setText("Edit Car");
        populateFields(car);
    }

    private void populateFields(Car c) {
        brandField.setText(c.getBrand() != null ? c.getBrand() : "");
        modelField.setText(c.getModel() != null ? c.getModel() : "");
        yearField.setText(c.getYear() > 0 ? String.valueOf(c.getYear()) : "");
        categoryField.setText(c.getCategory() != null ? c.getCategory() : "");
        priceField.setText(c.getPricePerDay() > 0 ? String.valueOf(c.getPricePerDay()) : "");
        statusCombo.setValue(c.getStatus() != null ? c.getStatus() : "available");
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
        
        // Get values from form
        String brand = brandField.getText().trim();
        String model = modelField.getText().trim();
        String yearStr = yearField.getText().trim();
        String category = categoryField.getText().trim();
        String priceStr = priceField.getText().trim();
        String status = statusCombo.getValue();

        // Validation that doesn't throw
        if (brand.isEmpty() || model.isEmpty() || yearStr.isEmpty() || 
            category.isEmpty() || priceStr.isEmpty()) {
            statusLabel.setText("All fields are required.");
            return; // This is validation, not an exception
        }

        try {
            int year = Integer.parseInt(yearStr);
            double price = Double.parseDouble(priceStr);
            
            // Additional validation
            if (year < 1900 || year > 2100) {
                statusLabel.setText("Year must be between 1900 and 2100.");
                return;
            }
            
            if (price <= 0) {
                statusLabel.setText("Price must be greater than 0.");
                return;
            }

            if (!editMode) {
                addNewCar(brand, model, year, category, price, status);
            } else {
                editExistingCar(brand, model, year, category, price, status);
            }
            
        } catch (NumberFormatException e) {
            statusLabel.setText("Year and Price must be valid numbers.");
            return;
        }
    }

    private void addNewCar(String brand, String model, int year, String category, double price, String status) 
            throws Exception {
        
        // Create Car
        Car car = new Car(0, brand, model, year, category, price, status);
        boolean carAdded = carService.addCar(car);
        
        if (carAdded) {
            saved = true;
            close();
        } else {
            throw new Exception("Failed to add car to database.");
        }
    }

    private void editExistingCar(String brand, String model, int year, String category, double price, String status) 
            throws Exception {
        
        // Update Car
        editingCar.setBrand(brand);
        editingCar.setModel(model);
        editingCar.setYear(year);
        editingCar.setCategory(category);
        editingCar.setPricePerDay(price);
        editingCar.setStatus(status);

        boolean updated = carService.updateCar(editingCar);
        
        if (updated) {
            saved = true;
            close();
        } else {
            throw new Exception("Failed to update car in database.");
        }
    }

    private void close() {
        Stage s = (Stage) cancelBtn.getScene().getWindow();
        s.close();
    }
}