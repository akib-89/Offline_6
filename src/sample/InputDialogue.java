package sample;

import data.Car;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class InputDialogue implements Initializable {

    @FXML
    private TextField registration;

    @FXML
    private TextField yearMade;

    @FXML
    private TextField manufacturer;

    @FXML
    private TextField model;

    @FXML
    private TextField price;

    @FXML
    private TextField primaryColor;

    @FXML
    private TextField secondaryColor;

    @FXML
    private TextField optionalColor;


    private String imgLoc;
    private BooleanBinding valid;

    public BooleanBinding validProperty() {
        return valid;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        valid = new BooleanBinding() {
            {
                bind(registration.textProperty(),
                        model.textProperty(),
                        manufacturer.textProperty(),
                        price.textProperty(),
                        primaryColor.textProperty());
            }
            @Override
            protected boolean computeValue() {
                return !registration.getText().trim().isEmpty()
                        && yearMade.getText().trim().matches("\\d{4}")
                        && !manufacturer.getText().trim().isEmpty()
                        && !model.getText().trim().isEmpty()
                        && price.getText().trim().matches("\\d{0,7}(\\.\\d{0,4})?")
                        && !primaryColor.getText().trim().isEmpty();
            }
        };


        yearMade.setText("1990");
        price.setText("0");
        imgLoc = "src//resources//img//defaultImg.png";
    }

    public Car getCar(){
        String registrationNumber = this.registration.getText();
        int yearMade = Integer.parseInt(this.yearMade.getText());
        String manufacturer = this.manufacturer.getText();
        String model = this.model.getText();
        double price = Double.parseDouble(this.price.getText());
        String [] colours = new String[3];
        colours[0] = primaryColor.getText();
        colours[1] = secondaryColor.getText();
        colours[2] = optionalColor.getText();
        Car car = new Car(registrationNumber,yearMade,colours,manufacturer,model,price);
        car.setImageLoc(imgLoc);
        return car;
    }
    @FXML
    private void handleImg(){
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG" , "*.jpg"),
                new FileChooser.ExtensionFilter("PNG" , "*.png"),
                new FileChooser.ExtensionFilter("GIF", "*.gif"));
        File selected = chooser.showOpenDialog(yearMade.getScene().getWindow());

        if (selected != null){
            imgLoc = selected.toPath().toAbsolutePath().toString();
        }
    }

    public void showInfo(Car car) {
        registration.setText(car.getRegistrationNumber());
        registration.setDisable(true);
        yearMade.setText(Integer.toString(car.getYearMade()));
        yearMade.setDisable(true);
        manufacturer.setText(car.getManufacturer());
        manufacturer.setDisable(true);
        model.setText(car.getModel());
        model.setDisable(true);
        price.setText(Double.toString(car.getPrice()));
        primaryColor.setText(car.getColor(0));
        secondaryColor.setText(car.getColor(1));
        optionalColor.setText(car.getColor(2));
    }
}
