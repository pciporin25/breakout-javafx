package projectGreenout;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class GreenhouseGas extends ImageView {

    protected Ball sceneBall;

    public GreenhouseGas(Image image) {
        super(image);
    }

    public boolean checkForBallCollision() {
        if (this.getBoundsInParent().intersects(sceneBall.getBoundsInParent())) {
            sceneBall.brickCollision(this.getLayoutBounds().getWidth(),
                    this.getX(),
                    this.getLayoutBounds().getHeight(),
                    this.getY());
            return true;
        }
        return false;
    }

}
