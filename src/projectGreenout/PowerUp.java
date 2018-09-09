package projectGreenout;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PowerUp extends ImageView {

    private double speedY;
    private Raft sceneRaft;
    private Ball sceneBall;
    private Level thisLevel;
    private PowerUpType powerUpType;

    //randomly create a powerup
    public PowerUp(double startX, double startY, double speedY, Raft raft, Ball myBall, Level myLevel) {
        super();
        this.setX(startX);
        this.setY(startY);
        this.speedY = speedY;
        this.sceneRaft = raft;
        this.sceneBall = myBall;
        this.thisLevel = myLevel;

        int powerUpRandom = (int) (Math.random() * 3 + 1);
        if (powerUpRandom == 3) {
            this.powerUpType = PowerUpType.STRENGTH;
        }
        else if (powerUpRandom == 2) {
            this.powerUpType = PowerUpType.BALL;
        }
        else {
            this.powerUpType = PowerUpType.PADDLE;
        }

        loadImage();
    }

    private void loadImage() {
        //random number between 1 and 3
        Image image;
        if (this.powerUpType == PowerUpType.STRENGTH) {
            image = new Image(N2O.class.getClassLoader().getResourceAsStream("strength-power.gif"));
        }
        else if (this.powerUpType == PowerUpType.BALL) {
            image = new Image(N2O.class.getClassLoader().getResourceAsStream("ball-power.gif"));
        }
        else {
            image = new Image(N2O.class.getClassLoader().getResourceAsStream("paddle-power.gif"));
        }
        this.setImage(image);
    }

    //Power up should fall from origin until it reaches bottom of screen
    public boolean step(double elapsedTime, double sceneHeight) {
        if (this.getBoundsInParent().intersects(sceneRaft.getBoundsInParent())) {
            activatePowerUp();
            return true;
        }
        else if (this.getY() < sceneHeight) {
            this.setY(this.getY() + speedY * elapsedTime);
            return false;
        }
        else {
            return true;
        }
    }

    public void activatePowerUp() {
        //stronger ball
        if (this.powerUpType == PowerUpType.STRENGTH) {
            this.sceneBall.toggleStrengthMultiplier();
        }
        //extra ball
        else if (this.powerUpType == PowerUpType.BALL) {
            thisLevel.setIsExtraBall(true);
        }
        //faster paddle
        else {
            this.sceneRaft.toggleSpeedMultiplier();
        }
    }
}

enum PowerUpType {
    STRENGTH,
    BALL,
    PADDLE
}
