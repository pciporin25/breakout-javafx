package projectGreenout;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PowerUp extends ImageView {

    private double speedY;

    //randomly create a powerup
    public PowerUp(double startX, double startY, double speedY) {
        super(loadImage());
        this.setX(startX);
        this.setY(startY);
        this.speedY = speedY;
    }

    private static Image loadImage() {
        Image image = new Image(N2O.class.getClassLoader().getResourceAsStream("strength-power.gif"));
        return image;
    }

    //Power up should fall from origin until it reaches bottom of screen
    public boolean step(double elapsedTime, double sceneHeight) {
        if (this.getY() < sceneHeight) {
            this.setY(this.getY() + speedY * elapsedTime);
            return false;
        }
        else {
            return true;
        }
    }
}
