package projectGreenout;

import javafx.scene.image.Image;

public class CH4 extends GreenhouseGas {
    public CH4(Ball ball) {
        super(loadImage());
        this.sceneBall = ball;
        this.hitsRemaining = 2;
    }

    private static Image loadImage() {
        var image = new Image(CH4.class.getClassLoader().getResourceAsStream("ch4.gif"));
        return image;
    }

    @Override
    public boolean hitAndDestroy() {
        if (hitsRemaining>1) {
            this.setImage(new Image (N2O.class.getClassLoader().getResourceAsStream("ch4-hit1.gif")));
            hitsRemaining--;
            return false;
        }
        else if (hitsRemaining>0) {
            this.setImage(new Image (N2O.class.getClassLoader().getResourceAsStream("ch4-hit2.gif")));
            hitsRemaining--;
            return false;
        }
        else {
            return true;
        }
    }
}
