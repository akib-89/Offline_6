package data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Loader {
    private final static Loader instance = new Loader();
    private final static String filename = "src//resources//cars.txt";
    private final CarList carList;

    private Loader() {
        this.carList = new CarList();
    }

    public static Loader getInstance() {
        return instance;
    }

    public void read(){
        Path path = Paths.get(filename);
        try(BufferedReader input = Files.newBufferedReader(path)){
            String line;
            while ((line = input.readLine()) != null){
                String [] fields = line.split(",");
                String registrationNumber = fields[0];
                int yearMade = Integer.parseInt(fields[1]);
                String [] colors = new String[3];
                System.arraycopy(fields, 2, colors, 0, 3);
                String manufacture = fields[5];
                String model = fields[6];
                double price = Double.parseDouble(fields[7]);
                String imgLoc = fields[8];
                int stock = Integer.parseInt(fields[9]);
                Car car = new Car(registrationNumber,yearMade,colors,manufacture,model,price);
                car.setImageLoc(imgLoc);
                carList.addCar(car,stock);
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        System.out.println("file loaded successfully!!");
    }

    public void write(){
        Path path = Paths.get(filename);
        try(BufferedWriter writer = Files.newBufferedWriter(path)){
            for (Car car:carList.getCars()){
                writer.write(car.toString()+","+carList.getStock(car));
                writer.newLine();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        System.out.println("application closed successfully");
    }

    public CarList getCarList() {
        return carList;
    }
    public int getCarStock(Car car){
        return carList.getStock(car);
    }

    public boolean updateStock(Car car,int amount) {
        return carList.updateStock(car,amount);
    }
}
