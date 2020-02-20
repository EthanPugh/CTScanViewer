import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

/**
 * Manipulates images in various ways.
 *
 * @author Ethan Pugh
 */
public class ImageManipulator {

    /**
     * Resize an image to a new specified width and height.
     *
     * @param oldImage  The image to be resized.
     * @param newWidth  The width of the new image/resized image.
     * @param newHeight The height of the new image/resized image.
     * @return The same image but resized.
     */
    public static WritableImage resize(WritableImage oldImage, final float newWidth, final float newHeight) {
        final float oldWidth = (float) oldImage.getWidth();
        final float oldHeight = (float) oldImage.getHeight();
        WritableImage newImage = new WritableImage((int) Math.floor(newWidth), (int) Math.floor(newHeight));
        PixelWriter newImageW = newImage.getPixelWriter();
        PixelReader oldImageR = oldImage.getPixelReader();

        for (int x = 0; x < newWidth - 1; x++) {
            for (int y = 0; y < newHeight - 1; y++) {
                float xNew = (x * oldWidth / newWidth);
                float yNew = (y * oldHeight / newHeight);
                Color col = oldImageR.getColor((int) Math.floor(xNew), (int) Math.floor(yNew));
                newImageW.setColor(x, y, col);
            }
        }
        return newImage;
    }

    /**
     * Scale an image by a specified amount.
     *
     * @param oldImage The image to be scaled.
     * @param scale    The amount to increase/decrease the size of the image.
     * @return The same image but scaled accordingly.
     */
    public static WritableImage scale(WritableImage oldImage, final float scale) {
        final float newWidth = (float) (oldImage.getWidth() * scale);
        final float newHeight = (float) (oldImage.getHeight() * scale);

        return ImageManipulator.resize(oldImage, newWidth, newHeight);
    }

    /**
     * Generate the MIP out of a set of slices.
     *
     * @param slices The images to be used in generating the MIP.
     * @return A single image of the MIP.
     */
    public static WritableImage generateMIP(WritableImage[] slices) {
        final int width = (int) slices[0].getWidth();
        final int height = (int) slices[0].getHeight();
        WritableImage mip = new WritableImage(width, height);
        PixelWriter mipW = mip.getPixelWriter();
        PixelReader mipR = mip.getPixelReader();
        PixelReader sliceR;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                for (WritableImage slice : slices) {
                    Color c;
                    sliceR = slice.getPixelReader();
                    if (sliceR.getArgb(x, y) > mipR.getArgb(x, y)) {
                        c = sliceR.getColor(x, y);
                    } else {
                        c = mipR.getColor(x, y);
                    }
                    mipW.setColor(x, y, Color.color(c.getRed(), c.getGreen(), c.getBlue()));
                }
            }
        }
        return mip;
    }

}
