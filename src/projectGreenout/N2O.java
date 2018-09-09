package projectGreenout;

import javafx.scene.image.Image;

public class N2O extends GreenhouseGas {
    public N2O(Ball ball) {
        super(loadImage());
        this.sceneBall = ball;
    }

    private static Image loadImage() {
        var image = new Image(N2O.class.getClassLoader().getResourceAsStream("n2o.gif"));
        return image;
    }
}
