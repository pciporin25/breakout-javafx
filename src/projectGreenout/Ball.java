package projectGreenout;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

public class Ball extends ImageView {

    private double width;
    private double height;
    private double directionX;
    private double directionY;
    private boolean outOfPlay;
    private double startPosX;
    private double startPosY;

    public Ball(Image image, double myDirectionX, double myDirectionY, double posX, double posY) {
        super(image);
        this.outOfPlay = false;

        this.width = image.getWidth();
        this.height = image.getHeight();

        this.directionX = myDirectionX;
        this.directionY = myDirectionY;

        this.startPosX = posX - this.width;
        this.startPosY = posY;
        this.setX(startPosX);
        this.setY(startPosY);
    }

    public void step(double elapsedTime, int speed, double sceneWidth, double sceneHeight) {
        if (this.getX() + this.width > sceneWidth || this.getX() < 0) {
            this.directionX *= -1;
        }

        if (this.getY() < 0) {
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

    public void resetBall() {
        this.setY(startPosY);
        this.setX(startPosX);
        this.directionX = 0;
        this.directionY = 0;
        this.outOfPlay = true;
    }

    public void handleKeyInput (KeyCode code) {
        //check if raft is in bounds before processing movement
        if (code == KeyCode.SPACE && this.outOfPlay == true) {
            this.outOfPlay = false;
            this.directionX = 1;
            this.directionY = 1;
        }

    }

}
