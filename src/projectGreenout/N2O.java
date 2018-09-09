package projectGreenout;

import javafx.scene.image.Image;

public class N2O extends GreenhouseGas {
    public N2O(Ball ball) {
        super(loadImage());
        this.sceneBall = ball;
        this.hitsRemaining = 1;
    }

    private static Image loadImage() {
        var image = new Image(N2O.class.getClassLoader().getResourceAsStream("n2o.gif"));
        return image;
    }

    public boolean hitAndDestroy() {
        //if multiplier is greater than 1, this brick should be destroyed
        if (sceneBall.getStrengthMultiplier()<=1 && hitsRemaining>0) {
            this.setImage(new Image (N2O.class.getClassLoader().getResourceAsStream("n2o-hit.gif")));
            hitsRemaining--;
            return false;
        }
        else {
            return true;
        }
    }
}
