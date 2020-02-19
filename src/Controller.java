import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import jdk.nashorn.internal.ir.debug.ClassHistogramElement;

import java.io.IOException;

/**
 * Controller for the main window which displays various pieces of data from the CT scan.
 *
 * @author Ethan Pugh
 */
public class Controller {

    @FXML
    private ImageView imageViewX = new ImageView(), imageViewY = new ImageView(), imageViewZ = new ImageView();

    @FXML
    private Slider sliderSliceX, sliderSliceY, sliderSliceZ, sliderScaleX, sliderScaleY, sliderScaleZ;

    @FXML
    private Button buttonMIPX, buttonMIPY, buttonMIPZ;

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
        setupSliders();
        setupButtons();
    }

    /**
     * Sets the ImageViews to the first slice of each axis.
     */
    private void setupImages() {
        imageX = data.getSlice(0, Axis.X);
        imageY = data.getSlice(0, Axis.Y);
        imageZ = data.getSlice(0, Axis.Z);
        imageViewX.setImage(imageX);
        imageViewY.setImage(imageY);
        imageViewZ.setImage(imageZ);
    }

    /**
     * Sets up event handlers for each slider:
     * Slice sliders will retrieve a new image based on their value;
     * Scale sliders will send the currently displayed image to be scaled.
     */
    private void setupSliders() {
        sliderSliceX.setOnMouseDragged(event -> {
            imageX = data.getSlice(sliderSliceX.valueProperty().intValue(), Axis.X);
            imageViewX.setImage(imageX);
        });
        sliderSliceY.setOnMouseDragged(event -> {
            imageY = data.getSlice(sliderSliceY.valueProperty().intValue(), Axis.Y);
            imageViewY.setImage(imageY);
        });
        sliderSliceZ.setOnMouseDragged(event -> {
            imageZ = data.getSlice(sliderSliceZ.valueProperty().intValue(), Axis.Z);
            imageViewZ.setImage(imageZ);
        });
        sliderScaleX.setOnMouseDragged(event -> imageViewX.setImage(ImageManipulator.scale(imageX, sliderScaleX.valueProperty().floatValue())));
        sliderScaleY.setOnMouseDragged(event -> imageViewY.setImage(ImageManipulator.scale(imageY, sliderScaleY.valueProperty().floatValue())));
        sliderScaleZ.setOnMouseDragged(event -> imageViewZ.setImage(ImageManipulator.scale(imageZ, sliderScaleZ.valueProperty().floatValue())));

    }

    /**
     * Sets up event handlers for each MIP button:
     * Each button retrieves the MIP for the specified axis.
     */
    private void setupButtons() {
        buttonMIPX.setOnMouseClicked(event -> {
            imageX = data.getMIP(Axis.X);
            imageViewX.setImage(imageX);
        });
        buttonMIPY.setOnMouseClicked(event -> {
            imageY = data.getMIP(Axis.Y);
            imageViewY.setImage(imageY);
        });
        buttonMIPZ.setOnMouseClicked(event -> {
            imageZ = data.getMIP(Axis.Z);
            imageViewZ.setImage(imageZ);
        });
    }

}


