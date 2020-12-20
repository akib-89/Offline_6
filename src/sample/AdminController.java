package sample;

import data.Car;
import data.Loader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.ResourceBundle;

public class AdminController implements Initializable {
    @FXML
    public Button manufacturer;
    @FXML
    public Button registration;
    @FXML
    public Button revert;
    @FXML
    public Button add;
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
    private MenuItem edit;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        contextMenu = new ContextMenu();
        edit = new MenuItem("Edit");
        edit.setOnAction(this::showDialogue);
        contextMenu.getItems().add(edit);
        MenuItem delete = new MenuItem("Delete");
        delete.setOnAction(actionEvent -> {
            Car car = carList.getSelectionModel().getSelectedItem();
            deleteCar(car);
        });
        contextMenu.getItems().add(delete);
        MenuItem addStock = new MenuItem("Add stock");
        addStock.setOnAction(event -> {
            Car car = carList.getSelectionModel().getSelectedItem();
            updateCarStock(car);
        });
        add.setOnAction(this::showDialogue);


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
    private void paneButtonPressed(ActionEvent event){
        if (event.getSource().equals(registration)) {
            textField2.setVisible(false);
            textField1.setVisible(true);
            textField1.setPromptText("Registration Number");
        }else if (event.getSource().equals(manufacturer)){
            textField1.setVisible(true);
            textField1.setPromptText("Manufacturer");
            textField2.setVisible(true);
            textField2.setPromptText("Model");
        }
    }

    @FXML
    public void enterRevertPressed(ActionEvent event) {
        if (event.getSource().equals(enter)) {
            if (!textField2.isVisible()) {
                String reg = textField1.getText();
                carList.setItems(FXCollections.observableArrayList(Loader.getInstance().getCarList().searchCar(reg)));
                return;
            }

            String manufacturer = textField1.getText();
            String model = textField2.getText();

            ObservableList<Car> list = Loader.getInstance().getCarList().searchCar(manufacturer, model);
            carList.setItems(FXCollections.observableArrayList(list));
            if (!list.isEmpty()) {
                carList.getSelectionModel().selectFirst();
            }
        }else if (event.getSource().equals(revert)){
            carList.setItems(Loader.getInstance().getCarList().getCars());
        }

    }




    private void showDialogue(ActionEvent event) {
        Dialog<ButtonType> dialogue = new Dialog<>();
        dialogue.initStyle(StageStyle.UNDECORATED);
        dialogue.initOwner(manufacturer.getScene().getWindow());
        if (event.getSource().equals(edit)){
            dialogue.setHeaderText("Edit existing car");
        }else if (event.getSource().equals(add)){
            dialogue.setHeaderText("Add a car to database");
        }
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("inputDialogue.fxml"));
        try{
            dialogue.setDialogPane(loader.load());
            InputDialogue controller = loader.getController();
            if (event.getSource().equals(edit)){
                Car car = carList.getSelectionModel().getSelectedItem();
                controller.showInfo(car);
            }
        }catch (IOException e){
            System.out.println("error in loading dialogue");
            e.printStackTrace();
        }
        InputDialogue controller = loader.getController();
        dialogue.getDialogPane().getButtonTypes().addAll(ButtonType.OK,ButtonType.CANCEL);
        dialogue.getDialogPane().lookupButton(ButtonType.OK).disableProperty().bind(controller.validProperty().not());

        Optional<ButtonType> result = dialogue.showAndWait();
        if (result.isPresent() && result.get().equals(ButtonType.OK)){
            Car car = controller.getCar();
            if (event.getSource().equals(edit)){
                Car prev = carList.getSelectionModel().getSelectedItem();
                /*if (!prev.getImageLoc().equals("src//resources//img//defaultImg.png")){
                    try {
                        Files.deleteIfExists(Paths.get(prev.getImageLoc()));
                    } catch (IOException e) {
                        System.out.println("error in deleting file in edit");
                    }
                }
                String extension = car.getImageLoc().substring(car.getImageLoc().length()-4);
                Path destination = Paths.get("src//resources//img//"+car.getRegistrationNumber()+extension);
                Path source = Paths.get(car.getImageLoc());
                try {
                    Files.copy(source,destination, StandardCopyOption.REPLACE_EXISTING);
                    car.setImageLoc(destination.toString());
                } catch (IOException e) {
                    System.out.println("error in copying during edit");
                }
                prev.setColors(car.getColors());
                prev.setPrice(car.getPrice());
                prev.setImageLoc(car.getImageLoc());*/
                if (Loader.getInstance().editCar(prev,car)){
                    System.out.println("successfully edited the car");
                }else{
                    System.out.println("could not update the car");
                }
            } else if (event.getSource().equals(add)) {
                if (Loader.getInstance().addCar(car)){
                    System.out.println("car added");
                }else {
                    System.out.println("could no add car");
                }
            }
            carList.setItems(Loader.getInstance().getCarList().getCars());
         }

    }

    private void deleteCar(Car car) {
        if (Loader.getInstance().deleteCar(car)){
            System.out.println("car deleted successfully");
            return;
        }

        System.out.println("could not delete");
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
        carList.setItems(Loader.getInstance().getCarList().getCars());
    }
}
