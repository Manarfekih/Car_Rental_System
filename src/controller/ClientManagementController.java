package controller;

import model.Client;

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
import model.Client;
import service.ClientService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClientManagementController {

    @FXML private TableView<Client> clientsTable;
    @FXML private TableColumn<Client, Integer> colId;
    @FXML private TableColumn<Client, String> colName;
    @FXML private TableColumn<Client, String> colEmail;
    @FXML private TableColumn<Client, String> colPhone;
    @FXML private TableColumn<Client, String> colLicense;
    @FXML private TableColumn<Client, String> colAddress;

    @FXML private TextField searchField;
    @FXML private Button btnSearch;
    @FXML private Button btnRefresh;
    @FXML private Button btnAdd;
    @FXML private Button btnEdit;
    @FXML private Button btnDelete;
    @FXML private Label lblStatus;
    @FXML private ComboBox<String> sortCombo;


    private final ClientService clientService = new ClientService();
    private final ObservableList<Client> clientList = FXCollections.observableArrayList();

    @FXML
    public void initialize() throws Exception {
        // set up columns
        colId.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getClientId()).asObject());
        colName.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(nullSafe(c.getValue().getName())));
        colEmail.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(nullSafe(c.getValue().getEmail())));
        colPhone.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(nullSafe(c.getValue().getPhone())));
        colLicense.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(nullSafe(c.getValue().getLicenseNumber())));
        colAddress.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(nullSafe(c.getValue().getAddress())));

        // wire actions
        btnSearch.setOnAction(e -> { try { searchClients(); } catch (Exception ex) { showError("Search", ex.getMessage()); } });
        btnRefresh.setOnAction(e -> { try { loadClients(); } catch (Exception ex) { showError("Refresh", ex.getMessage()); } });
        btnAdd.setOnAction(e -> { try { openClientForm(null); } catch (Exception ex) { showError("Open Form", ex.getMessage()); } });
        btnEdit.setOnAction(e -> {
            Client sel = clientsTable.getSelectionModel().getSelectedItem();
            if (sel == null) { showError("Edit Client", "Please select a client."); return; }
            try { openClientForm(sel); } catch (Exception ex) { showError("Open Form", ex.getMessage()); }
        });
        btnDelete.setOnAction(e -> { try { deleteSelectedClient(); } catch (Exception ex) { showError("Delete", ex.getMessage()); } });

        loadClients();
        setupSortCombo();

    }

    
   
    private void setupSortCombo() {
        sortCombo.getItems().addAll("Name", "Email", "License");
        sortCombo.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> sortClients(newVal));
        sortCombo.getSelectionModel().selectFirst();
    }

    private void sortClients(String criteria) {
        if (clientList.isEmpty()) return;

        switch(criteria) {
            case "Email":
                FXCollections.sort(clientList, Client.SORT_BY_EMAIL);
                break;
            case "License":
                FXCollections.sort(clientList, Client.SORT_BY_LICENSE);
                break;
            default:
                FXCollections.sort(clientList, Comparator.comparing(Client::getName, String.CASE_INSENSITIVE_ORDER));
        }

        lblStatus.setText("Sorted by " + criteria);
    }


    private String nullSafe(String s) { return s == null ? "" : s; }

    
    public void loadClients() throws Exception {
        List<Client> all = clientService.getAllClients();

        // Prevent  duplicates based on email
        Set<String> emails = new HashSet<>();
        List<Client> unique = new ArrayList<>();
        //so the client will be added only if he has uniqye email
        for (Client c : all) {
            if (emails.add(c.getEmail())) { 
                unique.add(c);
            }
        }

        // Update TableView
        clientList.setAll(unique);
        clientsTable.setItems(clientList);

        lblStatus.setText("Loaded " + unique.size() + " clients");
    }

    
    



    public void searchClients() throws Exception {
        String kw = searchField.getText() == null ? "" : searchField.getText().trim().toLowerCase();
        if (kw.isEmpty()) { clientsTable.setItems(clientList); return; }
        
        
         //instaed of streams 
        List<Client> filtered = new ArrayList<>();
        for (Client c : clientList) {
            if ((c.getName() != null && c.getName().toLowerCase().contains(kw))
             || (c.getEmail() != null && c.getEmail().toLowerCase().contains(kw))
             || (c.getLicenseNumber() != null && c.getLicenseNumber().toLowerCase().contains(kw))) {
                filtered.add(c);
            }
        }
        clientsTable.setItems(FXCollections.observableArrayList(filtered));
    }

    public void deleteSelectedClient() throws Exception {
        Client sel = clientsTable.getSelectionModel().getSelectedItem();
        if (sel == null) { showError("Delete", "No client selected."); return; }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Delete client "+ sel.getName() +" ?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait();
        if (confirm.getResult() != ButtonType.YES) return;

        boolean ok = clientService.deleteClient(sel.getClientId());
        if (ok) {
            clientList.remove(sel);
            showInfo("Deleted", "Client deleted.");
        } else {
            showError("Delete", "Could not delete client.");
        }
    }

    private void openClientForm(Client clientToEdit) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/client_form.fxml"));
        Parent root = loader.load();

        ClientFormController formController = loader.getController();
        if (clientToEdit != null) formController.setEditMode(clientToEdit);

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.setTitle(clientToEdit == null ? "Add Client" : "Edit Client");
        stage.showAndWait();

        // refresh after close if saved
        if (formController.isSaved()) loadClients();
    }
    
    
    

    private void showError(String title, String message) {
        Alert a = new Alert(Alert.AlertType.ERROR); a.setTitle(title); a.setHeaderText(null); a.setContentText(message); a.showAndWait();
    }
    private void showInfo(String title, String message) {
        Alert a = new Alert(Alert.AlertType.INFORMATION); a.setTitle(title); a.setHeaderText(null); a.setContentText(message); a.showAndWait();
    }
}
