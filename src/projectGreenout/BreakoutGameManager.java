package projectGreenout;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.security.Key;

public class BreakoutGameManager extends Application
{
    public static final String BOUNCER_IMAGE = "ball.gif";
    public static final String RAFT_IMAGE = "raft.gif";
    public static final String LEVEL1_BACKGROUND = "level1.jpg";
    public static final int SCENE_WIDTH = 700;
    public static final int SCENE_HEIGHT = 400;
    public static final int FRAMES_PER_SECOND = 60;
    public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    public static final int BALL_SPEED = 60;
    public static final int RAFT_SPEED = 10;

    private Scene myScene;
    private Ball myBall;
    private Raft myRaft;
    private Level myLevel;

    private LongProperty livesRemaining = new SimpleLongProperty(3);
    private Text livesRemainingText;
    private boolean isGameOver = false;

    public static void main(String[] args)
    {
        launch(args);
    }

    public void start(Stage myStage)
    {
        var backgroundImage = new Image(this.getClass().getClassLoader().getResourceAsStream(LEVEL1_BACKGROUND));
        myScene = setupGame(backgroundImage);
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

    private Scene setupGame(Image background) {
        // create one top level collection to organize the things in the scene
        var root = new AnchorPane();

        // create a place to see the shapes
        //var scene = new Scene(root, width, height, new ImagePattern(background));

        var image = new Image(this.getClass().getClassLoader().getResourceAsStream(BOUNCER_IMAGE));
        myBall = new Ball(image, 1, 1);
        root.getChildren().add(myBall);

        image = new Image(this.getClass().getClassLoader().getResourceAsStream(RAFT_IMAGE));
        myRaft = new Raft(image, myBall);
        root.getChildren().add(myRaft);
        root.setBottomAnchor(myRaft, 0.0);

        this.livesRemainingText = new Text("Lives Remaining: " + livesRemaining);
        root.getChildren().add(livesRemainingText);
        root.setTopAnchor(livesRemainingText, 0.0);
        //https://stackoverflow.com/questions/34514694/display-variable-value-in-text-javafx
        livesRemainingText.textProperty().bind(Bindings.createStringBinding(() -> "Lives Remaining: " + livesRemaining.get(), livesRemaining));

        Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT, new ImagePattern(background));
        double brickProbs[] = {0.75, 0.2, 0.05};
        //create level with bricks, text, etc.
        this.myLevel = new Level(root, scene, myBall, myRaft, brickProbs);

        // respond to input
        scene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));

        return scene;
    }


    // Change properties of shapes to animate them
    private void step(double elapsedTime) {
        myBall.step(elapsedTime, BALL_SPEED, myScene.getWidth(), myScene.getHeight());
        myRaft.step();
        myLevel.step();

        if (myBall.getY() > myScene.getHeight()) {
            livesRemaining.set(livesRemaining.get() - 1);
            myBall.resetBall();
        }
        if (livesRemaining.get() <= 0) {
            this.isGameOver = true;
        }
    }

    public void handleKeyInput(KeyCode code) {
        myRaft.handleKeyInput(code, myScene.getWidth());
        myBall.handleKeyInput(code);
    }

}