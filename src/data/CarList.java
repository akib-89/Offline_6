package data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

public class CarList {
    private final ObservableMap<Car,Integer> cars;

    public CarList(){
        this.cars = FXCollections.observableHashMap();
    }

    public boolean addCar(Car car,int stock){
        Car newCar = this.searchCar(car.getRegistrationNumber());
        if (newCar != null){
            //no need to proceed the car is already in the list
            return false;
        }
        return this.cars.put(car,stock) == null;
    }

    public Car searchCar(String registrationNumber){
        for (Car c: this.cars.keySet()){
            if (c.getRegistrationNumber().equalsIgnoreCase(registrationNumber)){
                return c;
            }
        }
        return null;
    }

    public ObservableList<Car> searchCar(String manufacture, String model){
        ObservableList<Car> cars = FXCollections.observableArrayList();
        if (model.equalsIgnoreCase("any")){
             for (Car c : this.cars.keySet()) {
                 if (c.getManufacturer().equalsIgnoreCase(manufacture)){
                     cars.add(c);
                 }
             }
        }else {
            for (Car c : this.cars.keySet()) {
                if (c.getManufacturer().equalsIgnoreCase(manufacture) && c.getModel().equalsIgnoreCase(model)) {
                    cars.add(c);
                }
            }
        }
        return cars;
    }

    protected int getStock(Car car){
        if (cars.containsKey(car)){
            return cars.get(car);
        }
        return -1;
    }

    public ObservableList<Car> getCars() {
        return FXCollections.observableArrayList(cars.keySet());
    }


    public void removeAll(){
        this.cars.clear();
    }
}
