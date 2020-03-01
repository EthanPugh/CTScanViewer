import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;

import java.io.IOException;

/**
 * Controller for the main window which displays various pieces of data from the CT scan.
 * All code in this class is my own.
 *
 * @author Ethan Pugh (985094)
 */
public class SliceWindowController {

    @FXML
    private ImageView imageViewX = new ImageView(), imageViewY = new ImageView(), imageViewZ = new ImageView();

    @FXML
    private Slider sliderSliceX, sliderSliceY, sliderSliceZ, sliderScaleX, sliderScaleY, sliderScaleZ;

    @FXML
    private Button buttonMIPX, buttonMIPY, buttonMIPZ;

    @FXML
    private GridPane gridPaneThumbs;

    private WritableImage imageX, imageY, imageZ;

    private Volume data;

    /**
     * Sets up the Volume ready for use and initialises the images, sliders and buttons to work with the data.
     *
     * @throws IOException If the file isn't found.
     */
    @FXML
    private void initialize() throws IOException {
        data = new Volume("src/resources/CT_head.raw", 256, 256, 113);
        setupImages();
        setupThumbnails();
        setupButtons();
        setupSliders();
    }

    /**
     * Sets the ImageViews to the first slice of each axis.
     */
    private void setupImages() {
        imageX = ImageManipulator.resize(data.getSlice(0, Axis.X), 256, 256);
        imageY = ImageManipulator.resize(data.getSlice(0, Axis.Y), 256, 256);
        imageZ = ImageManipulator.resize(data.getSlice(0, Axis.Z), 256, 256);
        imageViewX.setImage(imageX);
        imageViewY.setImage(imageY);
        imageViewZ.setImage(imageZ);
    }

    /**
     * Sets the GridPane up with all the thumbnails.
     */
    private void setupThumbnails() {
        int r = 0;
        int c = 0;
        for (WritableImage slice : data.getThumbnails()) {
            ImageView view = new ImageView(ImageManipulator.resize(slice, 100, 100));
            if (c > 255) {
                c = 0;
                r++;
            }
            if (r < 1) view.setRotate(90);
            gridPaneThumbs.add(view, c, r);
            // System.out.println("Col: " + c + ", Row: " + r); // Debug line to test all slices printing.
            c++;
        }
    }

    /**
     * Sets up event handlers for each MIP button:
     * Each button retrieves the MIP for the specified axis.
     */
    private void setupButtons() {
        buttonMIPX.setOnMouseClicked(event -> {
            imageX = ImageManipulator.resize(data.getMIP(Axis.X), 256, 256);
            imageViewX.setImage(imageX);
        });
        buttonMIPY.setOnMouseClicked(event -> {
            imageY = ImageManipulator.resize(data.getMIP(Axis.Y), 256, 256);
            imageViewY.setImage(imageY);
        });
        buttonMIPZ.setOnMouseClicked(event -> {
            imageZ = ImageManipulator.resize(data.getMIP(Axis.Z), 256, 256);
            imageViewZ.setImage(imageZ);
        });
    }

    /**
     * Sets up event handlers for each slider:
     * Slice sliders will retrieve a new axis-specific image based on their value;
     * Scale sliders will send the currently displayed image to be scaled.
     */
    private void setupSliders() {
        sliderSliceX.setMax(data.getWidth() - 1);
        sliderSliceY.setMax(data.getHeight() - 1);
        sliderSliceZ.setMax(data.getDepth() - 1);
        sliderSliceX.setOnMouseDragged(event -> {
            imageX = ImageManipulator.resize(data.getSlice(sliderSliceX.valueProperty().intValue(), Axis.X), 256, 256);
            imageViewX.setImage(imageX);
            sliderScaleX.valueProperty().setValue(1);
        });
        sliderSliceY.setOnMouseDragged(event -> {
            imageY = ImageManipulator.resize(data.getSlice(sliderSliceY.valueProperty().intValue(), Axis.Y), 256, 256);
            imageViewY.setImage(imageY);
            sliderScaleY.valueProperty().setValue(1);

        });
        sliderSliceZ.setOnMouseDragged(event -> {
            imageZ = ImageManipulator.resize(data.getSlice(sliderSliceZ.valueProperty().intValue(), Axis.Z), 256, 256);
            imageViewZ.setImage(imageZ);
            sliderScaleZ.valueProperty().setValue(1);

        });
        sliderScaleX.setOnMouseDragged(event -> imageViewX.setImage(ImageManipulator.scale(imageX, sliderScaleX.valueProperty().floatValue())));
        sliderScaleY.setOnMouseDragged(event -> imageViewY.setImage(ImageManipulator.scale(imageY, sliderScaleY.valueProperty().floatValue())));
        sliderScaleZ.setOnMouseDragged(event -> imageViewZ.setImage(ImageManipulator.scale(imageZ, sliderScaleZ.valueProperty().floatValue())));
    }

}


