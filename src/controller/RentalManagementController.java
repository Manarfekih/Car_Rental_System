package controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Rental;
import service.RentalService;

import java.util.List;

public class RentalManagementController {

    @FXML private TableView<Rental> rentalsTable;
    @FXML private TableColumn<Rental, Integer> colId;
    @FXML private TableColumn<Rental, String> colClient;
    @FXML private TableColumn<Rental, String> colCar;
    @FXML private TableColumn<Rental, String> colStartDate;
    @FXML private TableColumn<Rental, String> colEndDate;
    @FXML private TableColumn<Rental, Double> colTotalPrice;
    @FXML private TableColumn<Rental, String> colStatus;

    @FXML private TextField searchField;
    @FXML private Button btnSearch, btnRefresh, btnAdd, btnEdit, btnDelete, btnReturn;
    @FXML private Label lblStatus;

    private final RentalService rentalService = new RentalService();

    @FXML
    public void initialize() throws Exception {
        setupColumns();
        setupButtonActions();
        loadRentals();
    }

    private void setupColumns() {
        colId.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getRentalId()));
        colClient.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getClient().getName()));
        colCar.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getCar().getBrand() + " " + c.getValue().getCar().getModel()));
        colStartDate.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getStartDate().toString()));
        colEndDate.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getEndDate().toString()));
        colTotalPrice.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getTotalPrice()));
        colTotalPrice.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                setText(empty ? null : String.format("$%.2f", price));
            }
        });
        colStatus.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getStatus()));
    }

    private void setupButtonActions() {
        btnSearch.setOnAction(e -> runWithThrows(this::handleSearch));
        btnRefresh.setOnAction(e -> runWithThrows(this::loadRentals));
        btnAdd.setOnAction(e -> runWithThrows(() -> openForm(null)));
        btnEdit.setOnAction(e -> runWithThrows(this::handleEdit));
        btnDelete.setOnAction(e -> runWithThrows(this::handleDelete));
        btnReturn.setOnAction(e -> runWithThrows(this::handleReturn));
    }

    private void loadRentals() throws Exception {
        List<Rental> rentals = rentalService.getAllRentals();
        rentalsTable.setItems(FXCollections.observableArrayList(rentals));
        lblStatus.setText("Loaded " + rentals.size() + " rentals");
    }

    private void handleSearch() throws Exception {
        String keyword = searchField.getText() == null ? "" : searchField.getText().trim();
        List<Rental> results;
        if (keyword.isEmpty()) {
            results = rentalService.getAllRentals();
        } else {
            results = rentalService.searchRental(keyword);
        }
        rentalsTable.setItems(FXCollections.observableArrayList(results));
        lblStatus.setText("Found " + results.size() + " rentals matching '" + keyword + "'");
    }


    private void handleEdit() throws Exception {
        Rental selected = rentalsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Edit Rental", "Please select a rental to edit.");
            return;
        }
        openForm(selected);
    }

    private void handleDelete() throws Exception {
        Rental selected = rentalsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Delete Rental", "Please select a rental to delete.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, 
            "Delete rental ID " + selected.getRentalId() + "?", ButtonType.YES, ButtonType.NO);
        confirm.setTitle("Confirm Delete");
        confirm.setHeaderText("Delete Rental");

        if (confirm.showAndWait().get() != ButtonType.YES) return;

        if (rentalService.deleteRental(selected.getRentalId())) {
            showInfo("Deleted", "Rental deleted successfully.");
            loadRentals();
        } else {
            showError("Delete Rental", "Could not delete rental.");
        }
    }

    private void handleReturn() throws Exception {
        Rental selected = rentalsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Return Car", "Select a rental to return.");
            return;
        }

        if (rentalService.returnCar(selected.getRentalId())) {
            showInfo("Returned", "Car returned successfully.");
            loadRentals();
        } else {
            showError("Return Car", "Failed to return car.");
        }
    }


    private void openForm(Rental rental) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/RentalForm.fxml"));
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(loader.load()));
        stage.setTitle(rental == null ? "Add Rental" : "Edit Rental");

        RentalFormController controller = loader.getController();
        if (rental != null) controller.setEditMode(rental);

        stage.showAndWait();

        if (controller.isSaved()) loadRentals();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Helper to wrap actions that throw Exception
    private void runWithThrows(ThrowingRunnable runnable) {
        try {
            runnable.run();
        } catch (Exception ex) {
            ex.printStackTrace();
            showError("Error", ex.getMessage());
        }
    }

    @FunctionalInterface
    private interface ThrowingRunnable {
        void run() throws Exception;
    }
}
