import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.io.*;

public class Volume {

    private int WIDTH, HEIGHT, DEPTH;
    private WritableImage mipX, mipY, mipZ;
    private WritableImage[] slicesX, slicesY, slicesZ;
    private short[][][] data;
    private int max, min;

    public Volume(String filename, int width, int height, int depth) throws IOException {
        this.WIDTH = width;
        this.HEIGHT = height;
        this.DEPTH = depth;
        setData(filename);
        setSlicesX();
        setSlicesY();
        setSlicesZ();
        setMIPX();
        setMIPY();
        setMIPZ();
    }

    private void setData(String filename) throws IOException {
        File file = new File(filename);
        DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
        data = new short[DEPTH][HEIGHT][WIDTH];
        max = Short.MIN_VALUE;
        min = Short.MAX_VALUE;
        short r;
        int b1, b2;
        for (int k = 0; k < DEPTH; k++) {
            for (int j = 0; j < HEIGHT; j++) {
                for (int i = 0; i < WIDTH; i++) {
                    b1 = (int) in.readByte() & 0xff;
                    b2 = (int) in.readByte() & 0xff;
                    r = (short) ((b2 << 8) | b1);
                    if (r < min) min = r;
                    if (r > max) max = r;
                    data[k][j][i] = r;
                }
            }
        }
        System.out.println("Min: " + min + ", Max: " + max);
        in.close();
    }

    private void setSlicesX() {
        slicesX = new WritableImage[WIDTH];
        short datum;
        float col;
        for (int i = 0; i < WIDTH; i++) {
            WritableImage image = new WritableImage(DEPTH, HEIGHT);
            PixelWriter imageWriter = image.getPixelWriter();
            for (int j = 0; j < HEIGHT; j++) {
                for (int k = 0; k < DEPTH; k++) {
                    datum = data[k][j][i];
                    col = (((float) datum - (float) min) / ((float) (max - min)));
                    imageWriter.setColor(k, j, Color.color(col, col, col, 1.0));
                }
            }
            slicesX[i] = image;
        }
    }

    private void setSlicesY() {
        slicesY = new WritableImage[HEIGHT];
        short datum;
        float col;
        for (int j = 0; j < HEIGHT; j++) {
            WritableImage image = new WritableImage(WIDTH, DEPTH);
            PixelWriter imageWriter = image.getPixelWriter();
            for (int k = 0; k < DEPTH; k++) {
                for (int i = 0; i < WIDTH; i++) {
                    datum = data[k][j][i];
                    col = (((float) datum - (float) min) / ((float) (max - min)));
                    imageWriter.setColor(i, k, Color.color(col, col, col, 1.0));
                }
            }
            slicesY[j] = image;
        }
    }

    private void setSlicesZ() {
        slicesZ = new WritableImage[DEPTH];
        short datum;
        float col;
        for (int k = 0; k < DEPTH; k++) {
            WritableImage image = new WritableImage(WIDTH, HEIGHT);
            PixelWriter imageWriter = image.getPixelWriter();
            for (int i = 0; i < WIDTH; i++) {
                for (int j = 0; j < HEIGHT; j++) {
                    datum = data[k][j][i];
                    col = (((float) datum - (float) min) / ((float) (max - min)));
                    imageWriter.setColor(i, j, Color.color(col, col, col, 1.0));
                }
            }
            slicesZ[k] = image;
        }
    }

    private void setMIPX() {
        WritableImage image = new WritableImage(DEPTH, HEIGHT);
        PixelWriter imageWriter = image.getPixelWriter();
        short datum;
        float colMax;
        for (int j = 0; j < HEIGHT; j++) {
            for (int k = 0; k < DEPTH; k++) {
                colMax = -max;
                for (int i = 0; i < WIDTH; i++) {
                    datum = data[k][j][i];
                    colMax = Float.max((((float) datum - (float) min) / ((float) (max - min))), colMax);
                    imageWriter.setColor(k, j, Color.color(colMax, colMax, colMax, 1.0));
                }
            }
        }
        mipX = image;
    }

    private void setMIPY() {
        WritableImage image = new WritableImage(WIDTH, DEPTH);
        PixelWriter imageWriter = image.getPixelWriter();
        short datum;
        float colMax;
        for (int k = 0; k < DEPTH; k++) {
            for (int i = 0; i < WIDTH; i++) {
                colMax = -max;
                for (int j = 0; j < HEIGHT; j++) {
                    datum = data[k][j][i];
                    colMax = Float.max((((float) datum - (float) min) / ((float) (max - min))), colMax);
                    imageWriter.setColor(i, k, Color.color(colMax, colMax, colMax, 1.0));
                }
            }
        }
        mipY = image;
    }

    private void setMIPZ() {
        WritableImage image = new WritableImage(WIDTH, HEIGHT);
        PixelWriter imageWriter = image.getPixelWriter();
        short datum;
        float colMax;
        for (int j = 0; j < HEIGHT; j++) {
            for (int i = 0; i < WIDTH; i++) {
                colMax = -max;
                for (int k = 0; k < DEPTH; k++) {
                    datum = data[k][j][i];
                    colMax = Float.max((((float) datum - (float) min) / ((float) (max - min))), colMax);
                    imageWriter.setColor(i, j, Color.color(colMax, colMax, colMax, 1.0));
                }
            }
        }
        mipZ = image;
    }

    public WritableImage getScaledSliceX(WritableImage oldImage, float scaleX, float scaleY, int sliceVal) {
        final float oldWidth = (int) oldImage.getWidth();
        final float newWidth = (int) (oldWidth * scaleX);
        final float oldHeight = (int) oldImage.getHeight();
        final float newHeight = (int) (oldHeight * scaleY);
        System.out.println("Old: " + oldWidth + ", " + oldHeight);
        System.out.println("New: " + newWidth + ", " + newHeight);

        WritableImage newImage = new WritableImage((int) newWidth, (int) newHeight);
        PixelWriter newImageW = newImage.getPixelWriter();
        short datum;

        for (int k = 0; k < newWidth; k++) {
            for (int j = 0; j < newHeight; j++) {
                float x = (k * oldWidth / newWidth);
                float y = (j * oldHeight / newHeight);
                datum = data[(int) Math.floor(x)][(int) Math.floor(y)][sliceVal];
                float col = (((float) datum - (float) min) / ((float) (max - min)));
                newImageW.setColor(k, j, Color.color(col, col, col, 1.0));
            }
        }
        return newImage;
    }

    public WritableImage getScaledSliceY(WritableImage oldImage, float scaleX, float scaleY, int sliceVal) {
        final float oldWidth = (int) oldImage.getWidth();
        final float newWidth = (int) (oldWidth * scaleX);
        final float oldHeight = (int) oldImage.getHeight();
        final float newHeight = (int) (oldHeight * scaleY);
        System.out.println("Old: " + oldWidth + ", " + oldHeight);
        System.out.println("New: " + newWidth + ", " + newHeight);

        WritableImage newImage = new WritableImage((int) newWidth, (int) newHeight);
        PixelWriter newImageW = newImage.getPixelWriter();
        short datum;

        for (int i = 0; i < newWidth; i++) {
            for (int k = 0; k < newHeight; k++) {
                float x = (i * oldWidth / newWidth);
                float y = (k * oldHeight / newHeight);
                datum = data[(int) Math.floor(y)][sliceVal][(int) Math.floor(x)];
                float col = (((float) datum - (float) min) / ((float) (max - min)));
                newImageW.setColor(i, k, Color.color(col, col, col, 1.0));

            }
        }
        return newImage;
    }

    public WritableImage getScaledSliceZ(WritableImage oldImage, float scaleX, float scaleY, int sliceVal) {
        final float oldWidth = (int) oldImage.getWidth();
        final float newWidth = (int) (oldWidth * scaleX);
        final float oldHeight = (int) oldImage.getHeight();
        final float newHeight = (int) (oldHeight * scaleY);
        System.out.println("Old: " + oldWidth + ", " + oldHeight);
        System.out.println("New: " + newWidth + ", " + newHeight);

        WritableImage newImage = new WritableImage((int) newWidth, (int) newHeight);
        PixelWriter newImageW = newImage.getPixelWriter();
        short datum;

        for (int i = 0; i < newWidth; i++) {
            for (int j = 0; j < newHeight; j++) {
                float x = (i * oldWidth / newWidth);
                float y = (j * oldHeight / newHeight);

                datum = data[sliceVal][(int) Math.floor(y)][(int) Math.floor(x)];
                float col = (((float) datum - (float) min) / ((float) (max - min)));
                newImageW.setColor(i, j, Color.color(col, col, col, 1.0));

            }
        }
        return newImage;
    }

    public WritableImage getSlice(int val, Axis a) {
        if (a.equals(Axis.X)) {
            return slicesX[val];
        } else if (a.equals(Axis.Y)) {
            return slicesY[val];
        } else {
            return slicesZ[val];
        }
    }

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


