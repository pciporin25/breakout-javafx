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

}
