import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

/**
 * Manipulates images in various ways.
 * All code in this class is my own.
 *
 * @author Ethan Pugh
 */
public class ImageManipulator {

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
        WritableImage newImage = new WritableImage((int) (newWidth), (int) (newHeight));
        PixelWriter newImageW = newImage.getPixelWriter();
        PixelReader oldImageR = oldImage.getPixelReader();


        for (int x = 0; x < newWidth - 1; x++) {
            for (int y = 0; y < newHeight - 1; y++) {
                float oldX = x * oldWidth / newWidth;
                float oldY = y * oldHeight / newHeight;

                int x0 = (int) Math.floor(oldX);
                int y0 = (int) Math.floor(oldY);
                int x1 = x0 + 1;
                int y1 = y0 + 1;

                if (x0 >= oldWidth - 1 || y0 >= oldHeight - 1) {
                    newImageW.setColor(x, y, oldImageR.getColor((int) Math.floor(oldX),(int) Math.floor(oldY)));
                } else {
                    Color C00 = oldImageR.getColor(x0, y0);
                    Color C11 = oldImageR.getColor(x1, y1);

                    double midX = C00.getRed() + (C11.getRed() - C00.getRed()) * ((oldX - x0) / (x1 - x0));
                    double midY = C00.getRed() + (C11.getRed() - C00.getRed()) * ((oldY - y0) / (y1 - y0));
                    double col = midX + (midY - midX) * ((oldY - y0) / (y1 - y0));

                    newImageW.setColor(x, y, Color.color(col, col, col, 1.0));
                }
            }
        }
        return newImage;
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
                    sliceR = slice.getPixelReader();
                    Color c = sliceR.getArgb(x, y) > mipR.getArgb(x, y) ? sliceR.getColor(x, y) : mipR.getColor(x, y);
                    mipW.setColor(x, y, Color.color(c.getRed(), c.getGreen(), c.getBlue()));
                }
            }
        }
        return mip;
    }

    /**
     * Resize an image to a new specified width and height.
     *
     * @param oldImage  The image to be resized.
     * @param newWidth  The width of the new image/resized image.
     * @param newHeight The height of the new image/resized image.
     * @return The same image but resized.
     */
    public static WritableImage resizeNearest(WritableImage oldImage, final float newWidth, final float newHeight) {
        final float oldWidth = (float) oldImage.getWidth();
        final float oldHeight = (float) oldImage.getHeight();
        WritableImage newImage = new WritableImage((int) (newWidth), (int) (newHeight));
        PixelWriter newImageW = newImage.getPixelWriter();
        PixelReader oldImageR = oldImage.getPixelReader();

        for (int x = 0; x < newWidth - 1; x++) {
            for (int y = 0; y < newHeight - 1; y++) {
                float oldX = (x * oldWidth / newWidth);
                float oldY = (y * oldHeight / newHeight);
                newImageW.setColor(x, y, oldImageR.getColor((int) oldX, (int) oldY));
            }
        }
        return newImage;
    }

}
