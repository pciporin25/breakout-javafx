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
        //if ball has strength multiplier, subtract two hits
        if (hitsRemaining>1 && sceneBall.getStrengthMultiplier()>1) {
            this.setImage(new Image (N2O.class.getClassLoader().getResourceAsStream("ch4-hit2.gif")));
            hitsRemaining-=2;
            return false;
        }
        else if (hitsRemaining>1) {
            this.setImage(new Image (N2O.class.getClassLoader().getResourceAsStream("ch4-hit1.gif")));
            hitsRemaining--;
            return false;
        }
        //if ball does not have strength multiplier, treat as usual.  Otherwise, destroy.
        else if (hitsRemaining>0 && sceneBall.getStrengthMultiplier()<=1) {
            this.setImage(new Image (N2O.class.getClassLoader().getResourceAsStream("ch4-hit2.gif")));
            hitsRemaining--;
            return false;
        }
        else {
            return true;
        }
    }
}
