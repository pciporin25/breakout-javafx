package projectGreenout;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Ball extends ImageView {

    private double width;
    private double height;
    private double directionX;
    private double directionY;

    public Ball(Image image, double myDirectionX, double myDirectionY) {
        super(image);
        this.width = image.getWidth();
        this.height = image.getHeight();

        this.directionX = myDirectionX;
        this.directionY = myDirectionY;
    }

    public void step(double elapsedTime, int speed, double sceneWidth, double sceneHeight) {
        if (this.getX() + this.width > sceneWidth || this.getX() < 0) {
            this.directionX *= -1;
        }
        if (this.getY() + this.height > sceneHeight || this.getY() < 0) {
            this.directionY *= -1;
        }

        this.setX(this.getX() + speed * this.directionX * elapsedTime);
        this.setY(this.getY() + speed * this.directionY * elapsedTime);
    }

}
