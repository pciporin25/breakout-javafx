package projectGreenout;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Duration;

public class BreakoutGameManager extends Application
{
    public static final String BOUNCER_IMAGE = "ball.gif";
    public static final String RAFT_IMAGE = "raft.gif";
    public static final String LEVEL1_BACKGROUND = "level1.jpg";
    public static final int SCENE_SIZE = 500;
    public static final int FRAMES_PER_SECOND = 60;
    public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    public static final int BALL_SPEED = 60;
    public static final int RAFT_SPEED = 10;

    private Scene myScene;
    private Ball myBall;
    private Raft myRaft;

    public static void main(String[] args)
    {
        launch(args);
    }

    public void start(Stage myStage)
    {
        var backgroundImage = new Image(this.getClass().getClassLoader().getResourceAsStream(LEVEL1_BACKGROUND));
        myScene = setupGame(SCENE_SIZE, SCENE_SIZE, backgroundImage);
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

    private Scene setupGame(int width, int height, Image background) {
        // create one top level collection to organize the things in the scene
        var root = new AnchorPane();

        // create a place to see the shapes
        var scene = new Scene(root, width, height, new ImagePattern(background));

        var image = new Image(this.getClass().getClassLoader().getResourceAsStream(BOUNCER_IMAGE));
        myBall = new Ball(image, 1, 1);

        image = new Image(this.getClass().getClassLoader().getResourceAsStream(RAFT_IMAGE));
        myRaft = new Raft(image);
        root.setBottomAnchor(myRaft, 0.0);

        // order added to the group is the order in which they are drawn
        root.getChildren().add(myBall);
        root.getChildren().add(myRaft);


        var brick = new CO2();
        root.getChildren().add(brick);

        // respond to input
        scene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));

        return scene;
    }

    // Change properties of shapes to animate them
    private void step(double elapsedTime) {
        myBall.step(elapsedTime, BALL_SPEED, myScene.getWidth(), myScene.getHeight());
        myRaft.step(elapsedTime, RAFT_SPEED, myScene.getWidth(), myScene.getHeight());

        //check for collision, and handle it if there is one
        if (myRaft.getBoundsInParent().intersects(myBall.getBoundsInParent())) {
            myBall.raftCollision(myRaft.getLayoutBounds().getWidth(), myRaft.getX());
        }
    }

    private void handleKeyInput (KeyCode code) {
        //check if raft is in bounds before processing movement
        if (code == KeyCode.RIGHT && (myRaft.getX() + myRaft.getLayoutBounds().getWidth()) < myScene.getWidth()) {
            myRaft.setX(myRaft.getX() + RAFT_SPEED);
        }
        else if (code == KeyCode.LEFT && myRaft.getX() > 0) {
            myRaft.setX(myRaft.getX() - RAFT_SPEED);
        }
    }
}