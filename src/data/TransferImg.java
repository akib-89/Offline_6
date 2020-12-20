package data;

import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;

import java.io.Serializable;

public class TransferImg implements Serializable {
    private int height;
    private int width;
    private byte[] imgData;
    private static final long serialVersionUID = 1L;

    public WritableImage getImg(){
        WritableImage image = new WritableImage(width,height);
        image.getPixelWriter().setPixels(0,0,width,height, PixelFormat.getByteBgraInstance(),imgData,0,width*4);
        return image;
    }

    public void setImg(WritableImage image){
        if (height != image.getHeight() && width != image.getWidth()) {
            imgData = new byte[(int) image.getWidth() * (int) image.getHeight() * 4];
        }
        width = (int) image.getWidth();
        height = (int) image.getHeight();

        image.getPixelReader().getPixels(0, 0, width, height,
                PixelFormat.getByteBgraInstance(),
                imgData, 0, width * 4);
    }
}
