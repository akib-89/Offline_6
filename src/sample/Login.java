package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Login implements Initializable {
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Button login;
    @FXML
    private Label error;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        login.disableProperty().bind(username.textProperty().isEmpty().or(password.textProperty().isEmpty()));
        username.textProperty().addListener((observableValue, prev, last) ->{
            if (last.equalsIgnoreCase("Viewer")){
                login.disableProperty().unbind();
                login.setDisable(false);
                return;
            }

            if (!login.disableProperty().isBound()){
                login.disableProperty().bind(username.textProperty().isEmpty().or(password.textProperty().isEmpty()));
            }
        });
        login.setDefaultButton(true);
    }

    @FXML
    private void handleLogin() throws IOException {
        String user = username.getText();
        String pass = password.getText();

        if (user.equalsIgnoreCase("admin") && pass.equalsIgnoreCase("123")){
            //change the scene
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("admin.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
            username.getScene().getWindow().hide();
            return;
        }else if (user.equalsIgnoreCase("viewer")){
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("user.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
            username.getScene().getWindow().hide();
            return;
        }
        error.setText("Password or Username not valid");

    }
}
