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
    private Slider sliderX, sliderY, sliderZ, sliderXS, sliderYS, sliderZS;

    @FXML
    private Button buttonMIPX, buttonMIPY, buttonMIPZ;

    private WritableImage imageX, imageY, imageZ;
    private int sliceValX, sliceValY, sliceValZ;
    private float scaleValX, scaleValY, scaleValZ;
    private Volume data;

    @FXML
    private void initialize() throws IOException {
        data = new Volume("src/resources/CT_head.raw");
        setupImages();
        setupSliders();
        setupButtons();
    }

    private void setupImages() {
        imageX = data.getSliceX(0);
        imageY = data.getSliceY(0);
        imageZ = data.getSliceZ(0);
        imageViewX.setImage(imageX);
        imageViewY.setImage(imageY);
        imageViewZ.setImage(imageZ);
    }

    private void setupSliders() {
        sliderX.setOnMouseDragged(event -> {
            sliceValX = sliderX.valueProperty().intValue();
            imageX = data.getSliceX(sliceValX);
            imageViewX.setImage(imageX);
        });
        sliderY.setOnMouseDragged(event -> {
            sliceValY = sliderY.valueProperty().intValue();
            imageY = data.getSliceY(sliceValY);
            imageViewY.setImage(imageY);
        });
        sliderZ.setOnMouseDragged(event -> {
            sliceValZ = sliderZ.valueProperty().intValue();
            imageZ = data.getSliceZ(sliderZ.valueProperty().intValue());
            imageViewZ.setImage(imageZ);
        });
        sliderXS.setOnMouseReleased(event -> {
            scaleValX = sliderXS.valueProperty().floatValue();
            imageViewX.setImage(data.getScaledSliceX(imageX, scaleValX, scaleValX, sliceValX));
            imageViewX.setFitHeight(imageX.getHeight() * scaleValX);
            imageViewX.setFitWidth(imageX.getWidth() * scaleValX);
        });
        sliderYS.setOnMouseReleased(event -> {
            scaleValY = sliderYS.valueProperty().floatValue();
            imageViewY.setImage(data.getScaledSliceY(imageY, scaleValY, scaleValY, sliceValY));
            imageViewY.setFitHeight(imageY.getHeight() * scaleValY);
            imageViewY.setFitWidth(imageY.getWidth() * scaleValY);
        });
        sliderZS.setOnMouseReleased(event -> {
            scaleValZ = sliderZS.valueProperty().floatValue();
            imageViewZ.setImage(data.getScaledSliceZ(imageZ, scaleValZ, scaleValZ, sliceValZ));
            imageViewZ.setFitHeight(imageZ.getHeight() * scaleValZ);
            imageViewZ.setFitWidth(imageZ.getWidth() * scaleValZ);
        });

    }

    private void setupButtons() {
        buttonMIPX.setOnMouseClicked(event -> {
            imageX = data.getMipX();
            imageViewX.setImage(imageX);
        });
        buttonMIPY.setOnMouseClicked(event -> {
            imageY = data.getMipY();
            imageViewY.setImage(imageY);
        });
        buttonMIPZ.setOnMouseClicked(event -> {
            imageZ = data.getMipZ();
            imageViewZ.setImage(imageZ);
        });
    }

}


