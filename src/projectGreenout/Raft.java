package projectGreenout;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

public class Raft extends ImageView {

    private Ball sceneBall;
    private Ball extraBall;
    private int speedMultiplier = 1;
    private int multiplierCountdown = -1;

    public Raft(Image image, Ball ball) {
        super(image);
        this.sceneBall = ball;
    }

    public void step() {
        if (this.getBoundsInParent().intersects(sceneBall.getBoundsInParent())) {
            sceneBall.raftCollision(this.getLayoutBounds().getWidth(),
                    this.getX(),
                    this.getLayoutBounds().getHeight(),
                    this.getY());
        }

        if (this.extraBall!=null && this.getBoundsInParent().intersects(extraBall.getBoundsInParent())) {
            extraBall.raftCollision(this.getLayoutBounds().getWidth(),
                    this.getX(),
                    this.getLayoutBounds().getHeight(),
                    this.getY());
        }

        if (multiplierCountdown>0) {
            multiplierCountdown--;
        }
        else if (multiplierCountdown!=-1) {
            toggleSpeedMultiplier();
        }
    }

    public void handleKeyInput (KeyCode code, double sceneWidth) {
        //check if raft is in bounds before processing movement
        if (code == KeyCode.RIGHT && (this.getX() + this.getLayoutBounds().getWidth()) < sceneWidth) {
            this.setX(this.getX() + BreakoutGameManager.RAFT_SPEED * this.speedMultiplier);
        }
        else if (code == KeyCode.LEFT && this.getX() > 0) {
            this.setX(this.getX() - BreakoutGameManager.RAFT_SPEED * this.speedMultiplier);
        }
    }

    public void addExtraBall(Ball ball) {
        this.extraBall = ball;
    }

    public int getSpeedMultiplier() {
        return this.speedMultiplier;
    }

    public void toggleSpeedMultiplier() {
        if (this.speedMultiplier == 1) {
            this.speedMultiplier = 2;
            this.multiplierCountdown = 1000;
        }
        else {
            this.speedMultiplier = 1;
            this.multiplierCountdown = -1;
        }
    }

}
