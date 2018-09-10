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
    private boolean isSecretBall;

    private int strengthMultiplier = 1;
    private int multiplierCountdown = -1;
    Image original;
    Image multiplied = new Image(N2O.class.getClassLoader().getResourceAsStream("ball-multiplied.gif"));


    public Ball(Image image, double myDirectionX, double myDirectionY, double posX, double posY, boolean isSecretBall) {
        super(image);
        this.original = image;
        this.outOfPlay = false;
        this.isSecretBall = isSecretBall;

        this.width = image.getWidth();
        this.height = image.getHeight();

        this.directionX = myDirectionX;
        this.directionY = myDirectionY;

        this.startPosX = posX - this.width;
        this.startPosY = posY;

        //Ball behaves differently for secret level
        if (this.isSecretBall) {
            this.startPosY = posY + 350;
            this.directionY = myDirectionY * -1;
        }

        this.setX(startPosX);
        this.setY(startPosY);

    }

    public void step(double elapsedTime, int speed, double sceneWidth, double sceneHeight) {
        if (this.getX() + this.width > sceneWidth || this.getX() < 0) {
            this.directionX *= -1;
        }

        if (this.isSecretBall && this.getY() < 0) {
            this.resetBall();
        }
        else if (this.getY() < 0) {
            this.directionY *= -1;
        }
        else if (this.isSecretBall && this.getY() >= sceneHeight) {
            this.directionY *= -1;
        }
        else if (this.getY() >= sceneHeight) {
            this.resetBall();
        }

        this.setX(this.getX() + speed * this.directionX * elapsedTime);
        this.setY(this.getY() + speed * this.directionY * elapsedTime);

        if (multiplierCountdown>0) {
            multiplierCountdown--;
        }
        else if (multiplierCountdown!=-1) {
            toggleStrengthMultiplier();
        }
    }

    public void raftCollision(double raftWidth,
                              double raftPositionX) {

        this.directionY *= -1;


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
                               double brickPositionX) {

        this.directionY *= -1;

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
            if (!this.isSecretBall) {
                this.directionX = 1;
                this.directionY = 1;
            }
            else {
                this.directionX = -1;
                this.directionY = -1;
            }
        }

    }

    public boolean getIsOutOfPlay() {
        return this.outOfPlay;
    }

    public int getStrengthMultiplier() {
        return this.strengthMultiplier;
    }

    public void toggleStrengthMultiplier() {
        if (this.strengthMultiplier == 1) {
            this.strengthMultiplier = 2;
            setImage(multiplied);
            this.multiplierCountdown = 1000;
        }
        else {
            this.strengthMultiplier = 1;
            setImage(original);
            this.multiplierCountdown = -1;
        }
    }


}
