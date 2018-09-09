package projectGreenout;

import javafx.scene.image.Image;

public class CO2 extends GreenhouseGas {

    public CO2(Ball ball) {
        super(loadImage());
        this.sceneBall = ball;
    }

    private static Image loadImage() {
        var image = new Image(CO2.class.getClassLoader().getResourceAsStream("co2.gif"));
        return image;
    }

}
