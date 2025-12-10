package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import model.User;

import java.io.IOException;

public class ClientDashboardController {

    @FXML
    private Label lblWelcome;

    @FXML
    private Button btnAvailableCars, btnMyRentals, btnProfile, btnLogout;

    @FXML
    private StackPane contentArea;

    private User loggedUser;

    public void setLoggedUser(User user) {
        this.loggedUser = user;
        lblWelcome.setText("Welcome, " + user.getUsername() + "!");

        try {
            loadAvailableCars();
        } catch (IOException e) {
            showLoadError("/view/ClientAvailableCars.fxml", e);
        }
        
    }

    
    @FXML
    public void initialize() {
        btnAvailableCars.setOnAction(e -> {
            try {
                loadAvailableCars();
            } catch (IOException ex) {
                showLoadError("/view/ClientAvailableCars.fxml", ex);
            }
        });

        btnMyRentals.setOnAction(e -> {
            try {
                loadMyRentals();
            } catch (IOException ex) {
                showLoadError("/view/MyRentals.fxml", ex);
            }
        });

        btnProfile.setOnAction(e -> {
            try {
                loadProfile();
            } catch (IOException ex) {
                showLoadError("/view/Profile.fxml", ex);
            }
        });

        btnLogout.setOnAction(e -> {
            try {
                logout();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    private void loadAvailableCars() throws IOException {
        loadView("/view/ClientAvailableCars.fxml", controller -> {
            ((ClientAvailableCarsController) controller).setLoggedUser(loggedUser);
        });
    }

    private void loadMyRentals() throws IOException {
        loadView("/view/MyRentals.fxml", controller -> {
            ((ClientMyRentalsController) controller).setLoggedUser(loggedUser);
        });
    }

    private void loadProfile() throws IOException {
        loadView("/view/Profile.fxml", controller -> {
            ((ClientProfileController) controller).setLoggedUser(loggedUser);
        });
    }

    private void loadView(String fxmlPath, java.util.function.Consumer<Object> initializer)
            throws IOException {

        java.net.URL url = getClass().getResource(fxmlPath);

        if (url == null) {
            url = ClassLoader.getSystemClassLoader().getResource(fxmlPath.substring(1));
        }

        if (url == null) {
            throw new IOException("FXML file not found: " + fxmlPath);
        }

        FXMLLoader loader = new FXMLLoader(url);
        VBox view = loader.load();
        Object controller = loader.getController();

        if (initializer != null && controller != null) {
            initializer.accept(controller);
        }

        contentArea.getChildren().setAll(view);
    }

    private void logout() throws IOException {
        Stage currentStage = (Stage) btnLogout.getScene().getWindow();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
        Parent loginRoot = loader.load();

        Stage loginStage = new Stage();
        loginStage.setScene(new Scene(loginRoot, 600, 400));
        loginStage.setTitle("Car Rental System - Login");
        loginStage.show();

        currentStage.close();
    }

    private void showLoadError(String fxml, Exception e) {
        Label lbl = new Label("Failed to load view: " + fxml);
        lbl.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
        VBox v = new VBox(lbl);
        v.setStyle("-fx-padding: 20;");
        contentArea.getChildren().setAll(v);

        e.printStackTrace();
    }
}
