package projectGreenout;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

public class Raft extends ImageView {

    private Ball sceneBall;

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
    }

    public void handleKeyInput (KeyCode code, double sceneWidth) {
        //check if raft is in bounds before processing movement
        if (code == KeyCode.RIGHT && (this.getX() + this.getLayoutBounds().getWidth()) < sceneWidth) {
            this.setX(this.getX() + BreakoutGameManager.RAFT_SPEED);
        }
        else if (code == KeyCode.LEFT && this.getX() > 0) {
            this.setX(this.getX() - BreakoutGameManager.RAFT_SPEED);
        }
    }

}
