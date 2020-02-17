import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class ImageManipulator {

    public static WritableImage resize(WritableImage oldImage, final float newWidth, final float newHeight) {
        final float oldWidth = (float) oldImage.getWidth();
        final float oldHeight = (float) oldImage.getHeight();
        WritableImage newImage = new WritableImage((int) newWidth, (int) newHeight);
        PixelWriter newImageW = newImage.getPixelWriter();
        PixelReader oldImageR = oldImage.getPixelReader();

        for (int x = 0; x < newWidth; x++) {
            for (int y = 0; y < newHeight; y++) {
                float xScaled = (x * oldWidth / newWidth);
                float yScaled = (y * oldHeight / newHeight);
                Color col = oldImageR.getColor((int) xScaled, (int) yScaled);
                newImageW.setColor(x, y, col);
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
        PixelWriter mipW = mip.getPixelWriter();
        PixelReader mipR = mip.getPixelReader();
        PixelReader sliceR;
        int colMax;

        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                for (WritableImage slice : slices) {
                    sliceR = slice.getPixelReader();
                    colMax = Integer.max(sliceR.getArgb(x, y), mipR.getArgb(x, y));
                    java.awt.Color c = new java.awt.Color(colMax);
                    mipW.setColor(x, y, Color.rgb(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha() / 255.0));
                }
            }
        }
        return mip;
    }

}
