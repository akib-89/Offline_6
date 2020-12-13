package sample;

import data.Car;
import data.Loader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ResourceBundle;

public class UserController implements Initializable {
    @FXML
    private ListView<Car> carList;
    @FXML
    private TextArea carDetails;
    @FXML
    private ImageView carImg;
    @FXML
    private TextField textField1;
    @FXML
    private TextField textField2;
    @FXML
    private TitledPane search;
    @FXML
    private Button enter;


    private ContextMenu contextMenu;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        contextMenu = new ContextMenu();
        MenuItem buy = new MenuItem("Buy");
        buy.setOnAction(actionEvent -> {
            Car car = carList.getSelectionModel().getSelectedItem();
            updateCarStock(car);
        });
        contextMenu.getItems().add(buy);


        carList.setItems(Loader.getInstance().getCarList().getCars());
        carList.getSelectionModel().selectedItemProperty().addListener((observableValue, oldCar, newCar) -> updateCarDetails(newCar));
        carList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        carList.getSelectionModel().selectFirst();
        carList.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Car> call(ListView<Car> carListView) {
                ListCell<Car> cell = new ListCell<>() {
                    @Override
                    protected void updateItem(Car car, boolean empty) {
                        super.updateItem(car, empty);
                        if (empty) {
                            setText(null);
                        } else {
                            setText(car.getManufacturer() + ", " + car.getModel() + ", " + car.getRegistrationNumber());
                        }
                    }
                };
                cell.emptyProperty().addListener((observableValue, wasEmpty, isNowEmpty) -> {
                    if (isNowEmpty) {
                        cell.setContextMenu(null);
                    } else {
                        cell.setContextMenu(contextMenu);
                    }
                });
                return cell;
            }
        });

        search.expandedProperty().addListener((observableValue, aBoolean, newValue) -> {
            if (!newValue) {
                textField2.setText("");
                textField2.setVisible(false);
                textField1.setText("");
                textField1.setVisible(false);
                enter.setDisable(true);
            }
        });
        textField1.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!newValue.isEmpty()){
                if (!textField2.isVisible()){
                    enter.setDisable(false);
                    return;
                }
                enter.setDisable(textField2.getText().isEmpty());
                return;
            }
            enter.setDisable(true);
        });

        textField2.textProperty().addListener((observableValue, oldValue, newValue) -> {

            if (!newValue.isEmpty()){
                enter.setDisable(textField1.getText().isEmpty());
                return;
            }
            enter.setDisable(true);
        });
        enter.setDisable(true);



    }


    @FXML
    private void handleRegPressed(ActionEvent event){

        textField2.setVisible(false);
        textField1.setVisible(true);
        textField1.setPromptText("Registration Number");
    }
    @FXML
    private void handleManPressed(ActionEvent event){

        textField1.setVisible(true);
        textField1.setPromptText("Manufacturer");
        textField2.setVisible(true);
        textField2.setPromptText("Model");
    }

    @FXML
    public void handleEnterPressed(ActionEvent event) {
        if (!textField2.isVisible()){
            String reg = textField1.getText();
            carList.setItems(FXCollections.observableArrayList(Loader.getInstance().getCarList().searchCar(reg)));
            return;
        }

        String manufacturer = textField1.getText();
        String model = textField2.getText();

        ObservableList<Car> list = Loader.getInstance().getCarList().searchCar(manufacturer,model);
        carList.setItems(FXCollections.observableArrayList(list));
        if (!list.isEmpty()){
            carList.getSelectionModel().selectFirst();
        }

    }
    @FXML
    public void handleRevertPressed(ActionEvent event){
        carList.setItems(Loader.getInstance().getCarList().getCars());
    }


    private void updateCarDetails(Car car){
        if (car != null) {
            carDetails.setText("Registration number: " + car.getRegistrationNumber() +
                    "\nManufacturing year: " + car.getYearMade() +
                    "\nManufacturer: " + car.getManufacturer() +
                    "\nModel: " + car.getModel() +
                    "\nPrice: " + car.getPrice() +
                    "\nStock: " + Loader.getInstance().getCarStock(car)
            );
            try {
                Image img = new Image(Files.newInputStream(Path.of(car.getImageLoc())));
                carImg.setImage(img);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            carDetails.setText("");
            carImg.setImage(null);
        }

    }


    private void updateCarStock(Car car){
        System.out.println("handling car stock");

        if (Loader.getInstance().updateStock(car,1)){
            System.out.println("stock updated successfully");
            updateCarDetails(car);
        }else{
            System.out.println("could not update the stock not enough cars");
        }
    }


}
