import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

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

}
