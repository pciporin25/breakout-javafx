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

    public void raftCollision(double raftWidth,
                              double raftPositionX,
                              double raftHeight,
                              double raftPositionY) {
        //if ball intersects with top half of raft, y direction negative (up)
        if (this.getY() > raftPositionY + (raftHeight / 2)) {
            this.directionY = -1;

        }
        //if ball intersects with bottom half of raft, y direction positive (down)
        else {
            this.directionY = 1;
        }


        //if ball intersects with second half of raft, x direction positive
        if (this.getX() > raftPositionX + (raftWidth/2)) {
            this.directionX = 1;
        }
        //if ball intersects with first half of raft, x direction negative
        else {
            this.directionX = -1;
        }
    }

    public void brickCollision(double brickWidth,
                               double brickPositionX,
                               double brickHeight,
                               double brickPositionY) {

        /*
        //if ball intersects with top half of brick, y direction negative (up)
        if (this.getY() > brickPositionY + (brickHeight / 2)) {
            this.directionY = -1;
        }
        //if ball intersects with bottom half of brick, y direction positive (down)
        else {
            this.directionY = 1;
        }
        */
        this.directionY = 1;

        //if ball intersects with right half of brick, x direction postive (right)
        if (this.getX() > brickPositionX + (brickWidth / 2)) {
            this.directionX = 1;
        }
        //if ball intersects with left half of brick, x direction negative (left)
        else {
            this.directionX = -1;
        }
    }

}
