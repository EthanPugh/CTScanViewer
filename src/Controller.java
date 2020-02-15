import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;

import java.io.IOException;

public class Controller {

    @FXML
    private ImageView imageX = new ImageView(), imageY = new ImageView(), imageZ = new ImageView();
    private WritableImage sliceX, sliceY, sliceZ;
    private int sliceXVal, sliceYVal, sliceZVal;
    private float scaleXVal, scaleYVal, scaleZVal;

    @FXML
    private Slider sliderX, sliderY, sliderZ, sliderXS, sliderYS, sliderZS;

    @FXML
    private Button buttonMIPX, buttonMIPY, buttonMIPZ;

    private Volume data;

    @FXML
    private void initialize() throws IOException {
        data = new Volume("src/resources/CThead.raw");
        setupImages();
        setupSliders();
        setupButtons();
    }

    private void setupImages() {
        sliceX = data.getSliceX(0);
        sliceY = data.getSliceY(0);
        sliceZ = data.getSliceZ(0);
        imageX.setImage(sliceX);
        imageY.setImage(sliceY);
        imageZ.setImage(sliceZ);
    }

    private void setupSliders() {
        sliderX.setOnMouseDragged(event -> {
            sliceXVal = sliderX.valueProperty().intValue();
            sliceX = data.getSliceX(sliceXVal);
            imageX.setImage(sliceX);
        });
        sliderY.setOnMouseDragged(event -> {
            sliceYVal = sliderY.valueProperty().intValue();
            sliceY = data.getSliceY(sliceYVal);
            imageY.setImage(sliceY);
        });
        sliderZ.setOnMouseDragged(event -> {
            sliceZVal = sliderZ.valueProperty().intValue();
            sliceZ = data.getSliceZ(sliderZ.valueProperty().intValue());
            imageZ.setImage(sliceZ);
        });
        sliderXS.setOnMouseReleased(event -> {
            scaleXVal = sliderXS.valueProperty().floatValue();
            imageX.setImage(data.getScaledSliceX(sliceX, scaleXVal, scaleXVal, sliceXVal));
            imageX.setFitHeight(sliceX.getHeight() * scaleXVal);
            imageX.setFitWidth(sliceX.getWidth() * scaleXVal);
        });
        sliderYS.setOnMouseReleased(event -> {
            scaleYVal = sliderYS.valueProperty().floatValue();
            imageY.setImage(data.getScaledSliceY(sliceY, scaleYVal, scaleYVal, sliceYVal));
            imageY.setFitHeight(sliceY.getHeight() * scaleYVal);
            imageY.setFitWidth(sliceY.getWidth() * scaleYVal);
        });
        sliderZS.setOnMouseReleased(event -> {
            scaleZVal = sliderZS.valueProperty().floatValue();
            imageZ.setImage(data.getScaledSliceZ(sliceZ, scaleZVal, scaleZVal, sliceZVal));
            imageZ.setFitHeight(sliceZ.getHeight() * scaleZVal);
            imageZ.setFitWidth(sliceZ.getWidth() * scaleZVal);
        });

    }

    private void setupButtons() {
        buttonMIPX.setOnMouseClicked(event -> {
            sliceX = data.getMipX();
            imageX.setImage(sliceX);
        });
        buttonMIPY.setOnMouseClicked(event -> {
            sliceY = data.getMipY();
            imageY.setImage(sliceY);
        });
        buttonMIPZ.setOnMouseClicked(event -> {
            sliceZ = data.getMipZ();
            imageZ.setImage(sliceZ);
        });
    }

}


