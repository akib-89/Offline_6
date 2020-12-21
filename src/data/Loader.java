package data;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Loader {
    private final static Loader instance = new Loader();
    private Socket socket;
    private final CarList carList;

    private Loader() {
        this.carList = new CarList();
    }

    public static Loader getInstance() {
        return instance;
    }
    public CarList getCarList() {
        return carList;
    }
    public int getCarStock(Car car){
        return carList.getStock(car);
    }

    public void  read(){
        connect();
        try(ObjectOutputStream writer = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream())){
            writer.writeObject("-get");
            Car car;
            carList.removeAll();
            boolean EOF = false;
            TransferImg transfer = (TransferImg) input.readObject();
            WritableImage image = transfer.getImg();
            Path defaultImgPath = Paths.get("src\\resources\\img\\defaultImg.png");
            File imageFile = new File(defaultImgPath.toString());
            RenderedImage img = SwingFXUtils.fromFXImage(image,null);
            try {
                ImageIO.write(img,"png",imageFile);
            } catch (IOException e) {
                System.out.println("error in writing default image");
            }

            while (!EOF){
                try{
                    car = (Car) input.readObject();
                    int stock = input.readInt();
                    carList.addCar(car,stock);
                    Path carPath = Paths.get(car.getImageLoc());
                    if (defaultImgPath.toAbsolutePath().compareTo(carPath.toAbsolutePath()) != 0){
                        transfer = (TransferImg) input.readObject();
                        image = transfer.getImg();
                        File other = new File(car.getImageLoc());
                        img = SwingFXUtils.fromFXImage(image,null);
                        try{
                            ImageIO.write(img,"png",other);
                        }catch (IOException e){
                            System.out.println("error in writing other files");
                        }
                    }

                }catch (EOFException e){
                    EOF = true;
                }
            }
        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        terminate();
    }


    public synchronized boolean updateStock(Car car,int amount) {
        connect();
        boolean result = false;
        try(ObjectInputStream reader = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream writer = new ObjectOutputStream(socket.getOutputStream())){
            writer.writeObject("-updateStock");
            writer.writeObject(car);
            writer.writeInt(amount);
            writer.flush();
            result = reader.readBoolean();
            socket.close();
            this.read();
        }catch (IOException e){
            e.printStackTrace();
        }
        return result;
    }

    public synchronized boolean deleteCar(Car car) {
        connect();
        boolean result = false;
        try(ObjectInputStream reader = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream writer = new ObjectOutputStream(socket.getOutputStream())){
            writer.writeObject("-delete");
            writer.writeObject(car);
            writer.flush();
            result = reader.readBoolean();
            socket.close();
            this.read();
        }catch (IOException e){
            e.printStackTrace();
        }
        return result;
    }

    public boolean editCar(Car prev,Car present){
        connect();

        boolean result = false;
        try(ObjectOutputStream writer = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream reader = new ObjectInputStream(socket.getInputStream())){
            writer.writeObject("-edit");
            writer.writeObject(prev);
            writer.writeObject(present);
            Image image = new Image(Files.newInputStream(Paths.get(present.getImageLoc())));
            WritableImage wImg = clone(image);
            TransferImg tImg = new TransferImg();
            tImg.setImg(wImg);
            writer.writeObject(tImg);
            writer.flush();
            result = reader.readBoolean();
        }catch (IOException e){
            e.printStackTrace();
        }
        terminate();
        this.read();
        return result;
    }

    public boolean addCar(Car car){
        connect();
        boolean result = false;
        try(ObjectOutputStream writer = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream reader = new ObjectInputStream(socket.getInputStream())){
            writer.writeObject("-add");
            writer.writeObject(car);
            Image image = new Image(Files.newInputStream(Paths.get(car.getImageLoc())));
            WritableImage wImg = clone(image);
            TransferImg eImg = new TransferImg();
            eImg.setImg(wImg);
            writer.writeObject(eImg);
            writer.flush();
            result = reader.readBoolean();
        }catch (IOException e){
            e.printStackTrace();
        }
        System.out.println(result);
        terminate();
        this.read();
        return result;
    }

    private void connect(){
        try{
            socket = new Socket("192.168.0.105",60000);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void terminate(){
        try{
            socket.close();
        }catch (IOException e){
            System.out.println("error in terminating socket");
        }
    }

    private static WritableImage clone(Image image) {
        int height = (int) image.getHeight();
        int width = (int) image.getWidth();
        WritableImage writableImage = new WritableImage(width,height);
        PixelWriter pixelWriter = writableImage.getPixelWriter();
        if (pixelWriter == null) {
            throw new IllegalStateException("IMAGE_PIXEL_READER_NOT_AVAILABLE");
        }

        final PixelReader pixelReader = image.getPixelReader();
        if (pixelReader == null) {
            throw new IllegalStateException("IMAGE_PIXEL_READER_NOT_AVAILABLE");
        }
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = pixelReader.getColor(x, y);
                pixelWriter.setColor(x, y, color);
            }
        }
        return writableImage;
    }

    public boolean addStock(Car car, int amount) {
        connect();
        boolean result = false;
        try(ObjectInputStream reader = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream writer = new ObjectOutputStream(socket.getOutputStream())){
            writer.writeObject("-addStock");
            writer.writeObject(car);
            writer.writeInt(amount);
            writer.flush();
            result = reader.readBoolean();
            socket.close();
            this.read();
        }catch (IOException e){
            e.printStackTrace();
        }
        return result;
    }
}
