import javafx.scene.image.WritableImage;

public class Slice {

    private WritableImage image;
    private int sliceVal;
    private float scaleVal;

    public Slice () {

    }

    public void setImage(WritableImage image) {
        this.image = image;
    }

    public void setSliceVal(int sliceVal) {
        this.sliceVal = sliceVal;
    }

    public void setScaleVal(int scaleVal) {
        this.scaleVal = scaleVal;
    }



}
