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
import model.Employee;
import service.EmployeeService;

import java.util.ArrayList;
import java.util.List;

public class EmployeeManagementController {

    @FXML private TableView<Employee> employeesTable;
    @FXML private TableColumn<Employee, Integer> colId;
    @FXML private TableColumn<Employee, String> colUsername;
    @FXML private TableColumn<Employee, String> colEmail;
    @FXML private TableColumn<Employee, String> colPassword;

    @FXML private TextField searchField;
    @FXML private Button btnSearch;
    @FXML private Button btnRefresh;
    @FXML private Button btnAdd;
    @FXML private Button btnEdit;
    @FXML private Button btnDelete,btnSort;
    @FXML private Label lblStatus;
    @FXML private ComboBox<String> sortCombo; 


    private final EmployeeService employeeService = new EmployeeService();
    private final ObservableList<Employee> employeeList = FXCollections.observableArrayList();

    @FXML
    public void initialize() throws Exception {
        // Columns
        colId.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getUserId()).asObject());
        colUsername.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(nullSafe(c.getValue().getUsername())));
        colEmail.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(nullSafe(c.getValue().getEmail())));
        colPassword.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(nullSafe(c.getValue().getPassword())));

        // Button actions
        btnSearch.setOnAction(e -> {
            try { searchEmployees(); } catch (Exception ex) { showError("Search Employees", ex.getMessage()); }
        });
        btnRefresh.setOnAction(e -> {
            try { loadEmployees(); } catch (Exception ex) { showError("Load Employees", ex.getMessage()); }
        });
        btnAdd.setOnAction(e -> {
            try { openEmployeeForm(null); } catch (Exception ex) { showError("Add Employee", ex.getMessage()); }
        });
        btnEdit.setOnAction(e -> {
            Employee sel = employeesTable.getSelectionModel().getSelectedItem();
            if (sel == null) { showError("Edit Employee", "Please select an employee."); return; }
            try { openEmployeeForm(sel); } catch (Exception ex) { showError("Edit Employee", ex.getMessage()); }
        });
        btnDelete.setOnAction(e -> {
            try { deleteSelectedEmployee(); } catch (Exception ex) { showError("Delete Employee", ex.getMessage()); }
        });
        
        btnSort.setOnAction(e -> sortEmployeesByName());


        loadEmployees();

    }
    
    
    @FXML
    private void sortEmployeesByName() {
        FXCollections.sort(employeeList);  // Employee.compareTo() sorts by username
        employeesTable.setItems(FXCollections.observableArrayList(employeeList));
    }


    private String nullSafe(String s) { return s == null ? "" : s; }

    // Load all employees
    private void loadEmployees() throws Exception {
        List<Employee> all = employeeService.getAllEmployees();
        employeeList.setAll(all);
        employeesTable.setItems(employeeList);
        lblStatus.setText("Loaded " + all.size() + " employees");
    }

    // Search employees
    private void searchEmployees() throws Exception {
        String kw = searchField.getText() == null ? "" : searchField.getText().trim().toLowerCase();
        if (kw.isEmpty()) { employeesTable.setItems(employeeList); return; }

        List<Employee> filtered = new ArrayList<>();
        for (Employee e : employeeList) {
            if ((e.getUsername() != null && e.getUsername().toLowerCase().contains(kw)) ||
                (e.getEmail() != null && e.getEmail().toLowerCase().contains(kw))) {
                filtered.add(e);
            }
        }
        employeesTable.setItems(FXCollections.observableArrayList(filtered));
    }

    // Delete selected employee
    private void deleteSelectedEmployee() throws Exception {
        Employee sel = employeesTable.getSelectionModel().getSelectedItem();
        if (sel == null) { showError("Delete", "No employee selected."); return; }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete employee " + sel.getUsername() + " ?", 
                ButtonType.YES, ButtonType.NO);
        confirm.showAndWait();
        if (confirm.getResult() != ButtonType.YES) return;

        boolean ok = employeeService.deleteEmployee(sel.getUserId());
        if (ok) {
            employeeList.remove(sel);
            showInfo("Deleted", "Employee deleted successfully.");
        } else {
            showError("Delete", "Could not delete employee.");
        }
    }

    // Open Add/Edit employee form
    private void openEmployeeForm(Employee emp) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/EmployeeForm.fxml")); // adjust path
        Parent root = loader.load();

        EmployeeFormController form = loader.getController();
        if (emp != null) form.setEditMode(emp);

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.setTitle(emp == null ? "Add Employee" : "Edit Employee");
        stage.showAndWait();

        if (form.isSaved()) loadEmployees();
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
