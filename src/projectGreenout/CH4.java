package projectGreenout;

import javafx.scene.image.Image;

public class CH4 extends GreenhouseGas {
    public CH4() {
        super(loadImage());
    }

    private static Image loadImage() {
        var image = new Image(CH4.class.getClassLoader().getResourceAsStream("ch4.gif"));
        return image;
    }
}
