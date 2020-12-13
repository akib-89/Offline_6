package data;

public class Car {
    private final String registrationNumber;
    private final int yearMade;
    private final String manufacturer;
    private final String model;
    private String[] colors;
    private double price;
    private String imageLoc;

    public int getYearMade() {
        return yearMade;
    }

    public double getPrice() {
        return price;
    }

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

    public void setColors(String[] colors) {
        this.colors = colors;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String[] getColors() {
        return colors;
    }

    public void setImageLoc(String imageLoc) {
        this.imageLoc = imageLoc;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getModel() {
        return model;
    }

    public String getImageLoc() {
        return imageLoc;
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

    public String getColor(int index) {
        if (index<colors.length){
            return colors[index];
        }
        return null;
    }
/*@Override
    public boolean equals(Object obj) {
        if (obj == this ){
            return true;
        }
        if (obj instanceof Car){
            Car carObj = (Car)obj;
            return this.registrationNumber.equalsIgnoreCase(carObj.registrationNumber);
        }
        return false;
    }*/
}
