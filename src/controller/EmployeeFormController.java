package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Employee;
import service.EmployeeService;

public class EmployeeFormController {

    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private TextField passwordField;

    @FXML private Button saveBtn;
    @FXML private Button cancelBtn;
    @FXML private Label statusLabel;
    @FXML private Label titleLabel;

    private final EmployeeService employeeService = new EmployeeService();
    private Employee editEmployee;
    private boolean saved = false;

    // ---------------------------------------------------------
    // Initialize
    // ---------------------------------------------------------
    @FXML
    public void initialize() {
        saveBtn.setOnAction(e -> {
            try {
                saveEmployee();
            } catch (Exception ex) {
                statusLabel.setText(ex.getMessage()); // Show error in form
            }
        });
        cancelBtn.setOnAction(e -> closeForm());
    }

    // ---------------------------------------------------------
    // Set Edit mode
    // ---------------------------------------------------------
    public void setEditMode(Employee emp) {
        this.editEmployee = emp;
        titleLabel.setText("Edit Employee");
        usernameField.setText(emp.getUsername());
        emailField.setText(emp.getEmail());
        passwordField.setText(emp.getPassword());
    }

    // ---------------------------------------------------------
    // Save employee (add or edit)
    // ---------------------------------------------------------
    private void saveEmployee() throws Exception {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            throw new Exception("All fields are required!");
        }

        if (editEmployee == null) {
            // Add new employee
            Employee emp = new Employee();
            emp.setUsername(username);
            emp.setEmail(email);
            emp.setPassword(password);

            int newId = employeeService.addEmployee(emp); // throws Exception if email exists
            emp.setUserId(newId);
            saved = true;
        } else {
            // Edit existing employee
            editEmployee.setUsername(username);
            editEmployee.setEmail(email);
            editEmployee.setPassword(password);

            employeeService.updateEmployee(editEmployee); // throws Exception if email exists
            saved = true;
        }

        closeForm();
    }

    // ---------------------------------------------------------
    // Close form
    // ---------------------------------------------------------
    private void closeForm() {
        Stage stage = (Stage) saveBtn.getScene().getWindow();
        stage.close();
    }

    public boolean isSaved() {
        return saved;
    }
}
