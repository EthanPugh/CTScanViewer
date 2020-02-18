import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.io.*;

/**
 * Defines a single set of data for a 3D image or CT scan.
 *
 * @author Ethan Pugh
 */
public class Volume {

    private int width, height, depth;
    private WritableImage mipX, mipY, mipZ;
    private WritableImage[] slicesX, slicesY, slicesZ;
    private short[][][] data;
    private int max, min;

    /**
     * Constructor for a given volume of data.
     *
     * @param filename The path and name of the file to be read.
     * @param width    The width of the volume.
     * @param height   The height of the volume.
     * @param depth    The depth of the volume.
     * @throws IOException If the file is not found.
     */
    public Volume(String filename, int width, int height, int depth) throws IOException {
        long s = System.currentTimeMillis();
        this.width = width;
        this.height = height;
        this.depth = depth;
        setData(filename);
        setSlicesX();
        setSlicesY();
        setSlicesZ();
        setMIPX();
        setMIPY();
        setMIPZ();
        long f = System.currentTimeMillis();
        System.out.println("\nVolume created successfully in " + (f - s) + "ms.");
    }

    /**
     * Sets up the data for the volume and stores it in a 3D array.
     *
     * @param filename The path and name of the file to be read.
     * @throws IOException If the file is not found.
     */
    private void setData(String filename) throws IOException {
        File file = new File(filename);
        DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
        data = new short[depth][height][width];
        max = Short.MIN_VALUE;
        min = Short.MAX_VALUE;
        short r;
        int b1, b2;
        for (int k = 0; k < depth; k++) {
            for (int j = 0; j < height; j++) {
                for (int i = 0; i < width; i++) {
                    b1 = (int) in.readByte() & 0xff;
                    b2 = (int) in.readByte() & 0xff;
                    r = (short) ((b2 << 8) | b1);
                    if (r < min) min = r;
                    if (r > max) max = r;
                    data[k][j][i] = r;
                }
            }
        }
        in.close();
        System.out.println("CT image data generated successfully (Min: " + min + ", Max: " + max + ")");
    }

    /**
     * Generates all the x-axis images and stores them in the respective image array.
     */
    private void setSlicesX() {
        slicesX = new WritableImage[width];
        short datum;
        float col;
        for (int i = 0; i < width; i++) {
            WritableImage image = new WritableImage(depth, height);
            PixelWriter imageWriter = image.getPixelWriter();
            for (int j = 0; j < height; j++) {
                for (int k = 0; k < depth; k++) {
                    datum = data[k][j][i];
                    col = (((float) datum - (float) min) / ((float) (max - min)));
                    imageWriter.setColor(k, j, Color.color(col, col, col, 1.0));
                }
            }
            slicesX[i] = ImageManipulator.resize(image, width, height);
        }
        System.out.println("X-axis slices generated successfully");
    }

    /**
     * Generates all the y-axis images and stores them in the respective image array.
     */
    private void setSlicesY() {
        slicesY = new WritableImage[height];
        short datum;
        float col;
        for (int j = 0; j < height; j++) {
            WritableImage image = new WritableImage(width, depth);
            PixelWriter imageWriter = image.getPixelWriter();
            for (int k = 0; k < depth; k++) {
                for (int i = 0; i < width; i++) {
                    datum = data[k][j][i];
                    col = (((float) datum - (float) min) / ((float) (max - min)));
                    imageWriter.setColor(i, k, Color.color(col, col, col, 1.0));
                }
            }
            slicesY[j] = ImageManipulator.resize(image, width, height);
        }
        System.out.println("Y-axis slices generated successfully");
    }

    /**
     * Generates all the z-axis images and stores them in the respective image array.
     */
    private void setSlicesZ() {
        slicesZ = new WritableImage[depth];
        short datum;
        float col;
        for (int k = 0; k < depth; k++) {
            WritableImage image = new WritableImage(width, height);
            PixelWriter imageWriter = image.getPixelWriter();
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    datum = data[k][j][i];
                    col = (((float) datum - (float) min) / ((float) (max - min)));
                    imageWriter.setColor(i, j, Color.color(col, col, col, 1.0));
                }
            }
            slicesZ[k] = ImageManipulator.resize(image, width, height);
        }
        System.out.println("Z-axis slices generated successfully");
    }

    /**
     * Sets the MIP for the x-axis using the ImageManipulator.
     */
    private void setMIPX() {
        mipX = ImageManipulator.generateMIP(slicesX);
        System.out.println("X-axis MIP generated successfully");
    }

    /**
     * Sets the MIP for the y-axis using the ImageManipulator.
     */
    private void setMIPY() {
        mipY = ImageManipulator.generateMIP(slicesY);
        System.out.println("Y-axis MIP generated successfully");
    }

    /**
     * Sets the MIP for the z-axis using the ImageManipulator.
     */
    private void setMIPZ() {
        mipZ = ImageManipulator.generateMIP(slicesZ);
        System.out.println("Z-axis MIP generated successfully");
    }

    /**
     * Gets an image of a slice from one of the axes.
     *
     * @param val The slice number to get.
     * @param a   The axis to get the slice from.
     * @return An image of a slice.
     */
    public WritableImage getSlice(int val, Axis a) {
        if (a.equals(Axis.X)) {
            return slicesX[val];
        } else if (a.equals(Axis.Y)) {
            return slicesY[val];
        } else {
            return slicesZ[val];
        }
    }

    /**
     * Gets an image of the MIP for all slices in one axis.
     *
     * @param a The axis to get the MIP for.
     * @return An image of the MIP.
     */
    public WritableImage getMIP(Axis a) {
        if (a.equals(Axis.X)) {
            return mipX;
        } else if (a.equals(Axis.Y)) {
            return mipY;
        } else {
            return mipZ;
        }
    }

}


