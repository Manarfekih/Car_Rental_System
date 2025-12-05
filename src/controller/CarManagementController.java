package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Car;
import service.CarService;

import java.util.ArrayList;
import java.util.List;

public class CarManagementController {

    @FXML private TableView<Car> carTable;
    @FXML private TableColumn<Car, Integer> colCarId;
    @FXML private TableColumn<Car, String> colBrand;
    @FXML private TableColumn<Car, String> colModel;
    @FXML private TableColumn<Car, Integer> colYear;
    @FXML private TableColumn<Car, String> colCategory;
    @FXML private TableColumn<Car, Double> colPrice;
    @FXML private TableColumn<Car, String> colStatus;

    @FXML private TextField txtSearch;
    @FXML private Button btnSearch;
    @FXML private Button btnRefresh;
    @FXML private Button btnAdd;
    @FXML private Button btnUpdate;
    @FXML private Button btnDelete;
    @FXML private Label lblStatus;

    private final CarService carService = new CarService();
    private final ObservableList<Car> carList = FXCollections.observableArrayList();

    @FXML
    public void initialize() throws Exception {
        setupTableColumns();
        setupButtonActions();
        loadCars();
    }

    private void setupTableColumns() {
        colCarId.setCellValueFactory(c -> 
            new javafx.beans.property.SimpleIntegerProperty(c.getValue().getCarId()).asObject()
        );
        
        colBrand.setCellValueFactory(c -> 
            new javafx.beans.property.SimpleStringProperty(nullSafe(c.getValue().getBrand()))
        );
        
        colModel.setCellValueFactory(c -> 
            new javafx.beans.property.SimpleStringProperty(nullSafe(c.getValue().getModel()))
        );
        
        colYear.setCellValueFactory(c -> 
            new javafx.beans.property.SimpleIntegerProperty(c.getValue().getYear()).asObject()
        );
        
        colCategory.setCellValueFactory(c -> 
            new javafx.beans.property.SimpleStringProperty(nullSafe(c.getValue().getCategory()))
        );
        
        colPrice.setCellValueFactory(c -> 
            new javafx.beans.property.SimpleDoubleProperty(c.getValue().getPricePerDay()).asObject()
        );
        
        colPrice.setCellFactory(tc -> new TableCell<Car, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty || price == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", price));
                }
            }
        });
        
        colStatus.setCellValueFactory(c -> 
            new javafx.beans.property.SimpleStringProperty(nullSafe(c.getValue().getStatus()))
        );
    }

    private void setupButtonActions() {
        // All button actions now directly call methods that throw exceptions
        btnSearch.setOnAction(e -> {
            try {
                searchCars();
            } catch (Exception ex) {
                // Re-throw as RuntimeException since JavaFX event handlers can't throw checked exceptions
                throw new RuntimeException(ex);
            }
        });
        
        btnRefresh.setOnAction(e -> {
            try {
                loadCars();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        
        btnAdd.setOnAction(e -> {
            try {
                openCarForm(null);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        
        btnUpdate.setOnAction(e -> {
            Car sel = carTable.getSelectionModel().getSelectedItem();
            if (sel == null) { 
                // This is a validation error, not an exception - show error dialog
                showError("Edit Car", "Please select a car to edit.");
                return; 
            }
            try { 
                openCarForm(sel); 
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        
        btnDelete.setOnAction(e -> {
            try { 
                deleteSelectedCar(); 
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    private String nullSafe(String s) { 
        return s == null ? "" : s; 
    }

    public void loadCars() throws Exception {
        List<Car> all = carService.getAllCars();
        carList.setAll(all);
        carTable.setItems(carList);
        lblStatus.setText("Loaded " + all.size() + " cars");
    }

    @FXML
    public void handleSearch() throws Exception {
        searchCars();
    }
    
    @FXML
    public void handleRefresh() throws Exception {
        loadCars();
    }

    public void searchCars() throws Exception {
        String keyword = txtSearch.getText() == null ? "" : txtSearch.getText().trim();
        
        if (keyword.isEmpty()) { 
            carTable.setItems(carList); 
            lblStatus.setText("Showing all " + carList.size() + " cars");
            return; 
        }

        List<Car> filtered = carService.searchCars(keyword);
        carTable.setItems(FXCollections.observableArrayList(filtered));
        lblStatus.setText("Found " + filtered.size() + " cars matching '" + keyword + "'");
    }

    public void deleteSelectedCar() throws Exception {
        Car selected = carTable.getSelectionModel().getSelectedItem();
        if (selected == null) { 
            throw new Exception("No car selected for deletion.");
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, 
            "Delete car " + selected.getBrand() + " " + selected.getModel() + " (ID: " + selected.getCarId() + ")?", 
            ButtonType.YES, ButtonType.NO);
        confirm.setTitle("Confirm Delete");
        confirm.setHeaderText("Delete Car");
        
        if (confirm.showAndWait().get() != ButtonType.YES) {
            return;
        }

        boolean ok = carService.deleteCar(selected.getCarId());
        if (ok) {
            carList.remove(selected);
            showInfo("Deleted", "Car deleted successfully.");
            lblStatus.setText("Car deleted. Total cars: " + carList.size());
        } else {
            throw new Exception("Could not delete car from database.");
        }
    }

    private void openCarForm(Car carToEdit) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/car_form.fxml"));
        Parent root = loader.load();

        CarFormController formController = loader.getController();
        if (carToEdit != null) {
            formController.setEditMode(carToEdit);
        }

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.setTitle(carToEdit == null ? "Add New Car" : "Edit Car");
        stage.showAndWait();

        // Refresh after close if saved
        if (formController.isSaved()) {
            loadCars();
        }
    }
    
    // Back button 
    @FXML
    public void goBackToDashboard() throws Exception {
        Stage currentStage = (Stage) carTable.getScene().getWindow();
        if (currentStage == null) {
            throw new Exception("Cannot get current window stage.");
        }
        currentStage.close();
    }

    private void showError(String title, String message) {
        Alert a = new Alert(Alert.AlertType.ERROR); 
        a.setTitle(title); 
        a.setHeaderText(null); 
        a.setContentText(message); 
        a.showAndWait();
    }
    
    private void showInfo(String title, String message) {
        Alert a = new Alert(Alert.AlertType.INFORMATION); 
        a.setTitle(title); 
        a.setHeaderText(null); 
        a.setContentText(message); 
        a.showAndWait();
    }
}