package data;

import java.io.Serializable;

public class Car implements Serializable {
    private final String registrationNumber;
    private final int yearMade;
    private final String manufacturer;
    private final String model;
    private String[] colors;
    private double price;
    private String imageLoc;
    private static final long serialVersionUID = 1L;
    public Car(String registrationNumber, int yearMade, String[] colours, String manufacturer, String model, double price) {
        this.registrationNumber = registrationNumber;
        this.yearMade = yearMade;
        this.colors = colours;
        this.manufacturer = manufacturer;
        this.model = model;
        this.price = price;
        //src//sample//menu.....jpg;
        this.imageLoc = "src//resources//img//defaultImg.png";
    }
    public void setImageLoc(String imageLoc) {
        this.imageLoc = imageLoc;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    @Override
    public String toString() {
        return registrationNumber+ "," + yearMade + "," + colors[0] + "," +
                colors[1] + "," + colors[2] + "," + manufacturer + "," +
                model + "," + price + "," + imageLoc;
    }

    @Override
    public int hashCode() {
        return this.registrationNumber.hashCode() +
                this.manufacturer.hashCode()+
                this.model.hashCode();
    }
    @Override
    public boolean equals(Object obj) {
        if (obj == this ){
            return true;
        }
        if (obj instanceof Car){
            Car carObj = (Car)obj;
            return this.registrationNumber.equalsIgnoreCase(carObj.registrationNumber);
        }
        return false;
    }

    public int getYearMade() {
        return yearMade;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getModel() {
        return model;
    }

    public static Car parseCar(String line){
        String[] fields = line.split(",");
        String registrationNumber = fields[0];
        int yearMade = Integer.parseInt(fields[1]);
        String[] colors = new String[3];
        System.arraycopy(fields, 2, colors, 0, 3);
        String manufacture = fields[5];
        String model = fields[6];
        double price = Double.parseDouble(fields[7]);
        String imgLoc = fields[8];
        Car car = new Car(registrationNumber, yearMade, colors, manufacture, model, price);
        car.setImageLoc(imgLoc);
        return car;
    }

    public String getColor(int idx) {
        return colors[idx];
    }
    public String[] getColors(){
        return colors;
    }

    public void setColors(String[] colors) {
        this.colors = colors;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageLoc() {
        return imageLoc;
    }

}
