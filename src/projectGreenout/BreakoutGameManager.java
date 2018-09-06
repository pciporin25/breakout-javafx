package projectGreenout;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Duration;

public class BreakoutGameManager extends Application
{
    public static final String BOUNCER_IMAGE = "ball.gif";
    public static final String RAFT_IMAGE = "raft.gif";
    public static final int FRAMES_PER_SECOND = 60;
    public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    public static final int BALL_SPEED = 60;
    public static final int PADDLE_SPEED = 60;

    private Scene myScene;
    private Ball myBall;
    private Raft myRaft;

    public static void main(String[] args)
    {
        launch(args);
    }

    public void start(Stage myStage)
    {
        myScene = setupGame(400, 400, Color.AZURE);
        myStage.setTitle("Project GREENOUT: Greenhouse Gas Elimination");
        myStage.setScene(myScene);
        myStage.show();

        // attach "game loop" to timeline to play it
        var frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> step(SECOND_DELAY));
        var animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
    }

    private Scene setupGame(int width, int height, Paint background) {
        // create one top level collection to organize the things in the scene
        var root = new Group();

        // create a place to see the shapes
        var scene = new Scene(root, width, height, background);

        var image = new Image(this.getClass().getClassLoader().getResourceAsStream(BOUNCER_IMAGE));
        myBall = new Ball(image, 1, 1);

        image = new Image(this.getClass().getClassLoader().getResourceAsStream(RAFT_IMAGE));
        myRaft = new Raft(image);

        // order added to the group is the order in which they are drawn
        root.getChildren().add(myBall);
        root.getChildren().add(myRaft);

        return scene;
    }

    // Change properties of shapes to animate them
    private void step(double elapsedTime) {
        myBall.step(elapsedTime, BALL_SPEED, myScene.getWidth(), myScene.getHeight());
        myRaft.step(elapsedTime, PADDLE_SPEED, myScene.getWidth(), myScene.getHeight());
    }
}