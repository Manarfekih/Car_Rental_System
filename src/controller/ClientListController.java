package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Client;
import service.ClientService;

public class ClientListController {

    @FXML private TableView<Client> tableClients;
    @FXML private TableColumn<Client, Integer> colId;
    @FXML private TableColumn<Client, String> colName;
    @FXML private TableColumn<Client, String> colEmail;
    @FXML private TableColumn<Client, String> colPhone;
    @FXML private TableColumn<Client, String> colLicense;
    @FXML private TableColumn<Client, String> colAddress;

    @FXML private Button btnAdd;
    @FXML private Button btnEdit;
    @FXML private Button btnDelete;
    @FXML private TextField txtSearch;
    @FXML private Button btnSearch;

    private final ClientService clientService = new ClientService();
    private final ObservableList<Client> clientList = FXCollections.observableArrayList();

    @FXML
    public void initialize() throws Exception {
        setupColumns();
        refreshTable();

        btnAdd.setOnAction(e -> { try { openAddForm(); } catch (Exception ex) { showError(ex.getMessage()); }});
        btnEdit.setOnAction(e -> { try { openEditForm(); } catch (Exception ex) { showError(ex.getMessage()); }});
        btnDelete.setOnAction(e -> { try { deleteClient(); } catch (Exception ex) { showError(ex.getMessage()); }});
        btnSearch.setOnAction(e -> { try { searchClients(); } catch (Exception ex) { showError(ex.getMessage()); }});
    }

    private void setupColumns() {
        colId.setCellValueFactory(c -> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getClientId()));
        colName.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getName()));
        colEmail.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getEmail()));
        colPhone.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getPhone()));
        colLicense.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getLicenseNumber()));
        colAddress.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getAddress()));
    }

    private void refreshTable() throws Exception {
        clientList.setAll(clientService.getAllClients());
        tableClients.setItems(clientList);
    }

    private void openAddForm() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/client_form.fxml"));
        Stage stage = createFormStage(loader, "Add Client");

        stage.showAndWait();

        ClientFormController controller = loader.getController();
        if (controller.isSaved()) refreshTable();
    }

    private void openEditForm() throws Exception {
        Client selected = tableClients.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Select a client first.");
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/client_form.fxml"));
        Stage stage = createFormStage(loader, "Edit Client");

        ClientFormController controller = loader.getController();
        controller.setEditMode(selected);

        stage.showAndWait();
        if (controller.isSaved()) refreshTable();
    }

    private void deleteClient() throws Exception {
        Client selected = tableClients.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Select a client first.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete this client?",
                ButtonType.YES, ButtonType.NO);
        confirm.showAndWait();

        if (confirm.getResult() == ButtonType.YES) {
            clientService.deleteClient(selected.getClientId());
            refreshTable();
        }
    }

    private void searchClients() throws Exception {
        String keyword = txtSearch.getText().trim().toLowerCase();
        if (keyword.isEmpty()) {
            refreshTable();
            return;
        }

        // Non-stream filtering
        ObservableList<Client> filtered = FXCollections.observableArrayList();
        for (Client c : clientService.getAllClients()) {
            if ((c.getName() != null && c.getName().toLowerCase().contains(keyword)) ||
                (c.getEmail() != null && c.getEmail().toLowerCase().contains(keyword))) {
                filtered.add(c);
            }
        }
        tableClients.setItems(filtered);
    }

    private Stage createFormStage(FXMLLoader loader, String title) throws Exception {
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);
        return stage;
    }

    private void showWarning(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.show();
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Error");
        alert.setContentText(msg);
        alert.show();
    }
}
