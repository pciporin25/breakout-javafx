package projectGreenout;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class GreenhouseGas extends ImageView {

    protected Ball sceneBall;
    protected Ball extraBall;
    protected int hitsRemaining;

    public GreenhouseGas(Image image) {
        super(image);
    }

    public boolean checkForBallCollision() {
        if (this.getBoundsInParent().intersects(sceneBall.getBoundsInParent())) {
            sceneBall.brickCollision(this.getLayoutBounds().getWidth(),
                    this.getX(),
                    this.getLayoutBounds().getHeight(),
                    this.getY());
            return this.hitAndDestroy();
        }
        if (this.extraBall!=null && this.getBoundsInParent().intersects(extraBall.getBoundsInParent())) {
            extraBall.brickCollision(this.getLayoutBounds().getWidth(),
                    this.getX(),
                    this.getLayoutBounds().getHeight(),
                    this.getY());
            return this.hitAndDestroy();
        }
        return false;
    }

    public void addExtraBall(Ball ball) {
        this.extraBall = ball;
    }

    public abstract boolean hitAndDestroy();

}
