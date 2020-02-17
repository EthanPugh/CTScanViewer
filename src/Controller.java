import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;

import java.io.IOException;

public class Controller {

    @FXML
    private ImageView imageViewX = new ImageView(), imageViewY = new ImageView(), imageViewZ = new ImageView();

    @FXML
    private Slider sliderSliceX, sliderSliceY, sliderSliceZ, sliderScaleX, sliderScaleY, sliderScaleZ;

    @FXML
    private Button buttonMIPX, buttonMIPY, buttonMIPZ;

    private WritableImage imageX, imageY, imageZ;
    private int sliceValX, sliceValY, sliceValZ;
    private float scaleValX, scaleValY, scaleValZ;
    private Volume data;

    @FXML
    private void initialize() throws IOException {
        data = new Volume("src/resources/CT_head.raw", 256, 256, 113);
        setupImages();
        setupSliders();
        setupButtons();
    }

    private void setupImages() {
        imageX = data.getSlice(0, Axis.X);
        imageY = data.getSlice(0, Axis.Y);
        imageZ = data.getSlice(0, Axis.Z);
        imageViewX.setImage(imageX);
        imageViewY.setImage(imageY);
        imageViewZ.setImage(imageZ);
    }

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
        sliderScaleX.setOnMouseReleased(event -> {
            imageViewX.setImage(ImageManipulator.scale(imageX, sliderScaleX.valueProperty().floatValue()));
            imageViewX.setFitHeight(imageX.getHeight() * sliderScaleX.valueProperty().floatValue());
            imageViewX.setFitWidth(imageX.getWidth() * sliderScaleX.valueProperty().floatValue());
        });
        sliderScaleY.setOnMouseReleased(event -> {
            imageViewY.setImage(ImageManipulator.scale(imageY, sliderScaleY.valueProperty().floatValue()));
            imageViewY.setFitHeight(imageY.getHeight() * sliderScaleY.valueProperty().floatValue());
            imageViewY.setFitWidth(imageY.getWidth() * sliderScaleY.valueProperty().floatValue());
        });
        sliderScaleZ.setOnMouseReleased(event -> {
            imageViewZ.setImage(ImageManipulator.scale(imageZ, sliderScaleZ.valueProperty().floatValue()));
            imageViewZ.setFitHeight(imageZ.getHeight() * sliderScaleZ.valueProperty().floatValue());
            imageViewZ.setFitWidth(imageZ.getWidth() * sliderScaleZ.valueProperty().floatValue());
        });

    }

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


