import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import sun.awt.image.PixelConverter;

public class ImageManipulator {

    public static WritableImage resize(WritableImage oldImage, final float newWidth, final float newHeight) {
        final float oldWidth = (float) oldImage.getWidth();
        final float oldHeight = (float) oldImage.getHeight();

        WritableImage newImage = new WritableImage((int) newWidth, (int) newHeight);
        PixelWriter newImageWriter = newImage.getPixelWriter();
        PixelReader oldImageReader = oldImage.getPixelReader();

        for (int x = 0; x < newWidth; x++) {
            for (int y = 0; y < newHeight; y++) {
                float xScaled = (x * oldWidth / newWidth);
                float yScaled = (y * oldHeight / newHeight);
                Color col = oldImageReader.getColor((int) xScaled, (int) yScaled);
                newImageWriter.setColor(x, y, col);
            }
        }
        return newImage;
    }

    public static WritableImage scale(WritableImage oldImage, final float scale) {
        final float newWidth = (float) (oldImage.getWidth() * scale);
        final float newHeight = (float) (oldImage.getHeight() * scale);

        return ImageManipulator.resize(oldImage, newWidth, newHeight);
    }

    public static WritableImage generateMIP(WritableImage[] slices) {
        final int WIDTH = (int) slices[0].getWidth();
        final int HEIGHT = (int) slices[0].getHeight();
        WritableImage mip = new WritableImage(WIDTH, HEIGHT);
        PixelWriter mipWriter = mip.getPixelWriter();
        PixelReader mipReader = mip.getPixelReader();
        int colMax;
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                for (int i = 0; i < slices.length -1; i++) {
                    PixelReader sliceReader = slices[i].getPixelReader();
                    colMax = Integer.max(sliceReader.getArgb(x, y), mipReader.getArgb(x, y));
                    java.awt.Color c = new java.awt.Color(colMax);
                    float[] comps = new float[3];
                    comps = c.getColorComponents(comps);
                    mipWriter.setColor(x, y, Color.color(comps[0], comps[1], comps[2]));
                }
            }
        }
        return mip;
    }

}
