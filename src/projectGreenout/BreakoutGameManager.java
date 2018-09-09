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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BreakoutGameManager extends Application
{
    public static final String BOUNCER_IMAGE = "ball.gif";
    public static final String RAFT_IMAGE = "raft.gif";
    public static final int SCENE_WIDTH = 700;
    public static final int SCENE_HEIGHT = 400;
    public static final int FRAMES_PER_SECOND = 60;
    public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    public static final int BALL_SPEED = 60;
    public static final int RAFT_SPEED = 10;

    private Scene myScene;
    private Stage gameStage;
    private Ball myBall;
    private Ball myExtraBall;
    private Raft myRaft;
    private Level myLevel;
    private List<Level> myLevels;
    private Iterator<Level> myLevelIterator;
    private GameStatus myGameStatus;

    private LongProperty livesRemaining = new SimpleLongProperty(3);
    private Text livesRemainingText;
    private boolean isGameOver = false;

    public static void main(String[] args)
    {
        launch(args);
    }

    public void start(Stage myStage)
    {
        this.gameStage = myStage;
        this.myLevels = createLevels();
        this.myLevelIterator = this.myLevels.iterator();

        myScene = setupLevel();
        gameStage.setTitle("Project GREENOUT: Greenhouse Gas Elimination");
        gameStage.setScene(myScene);
        gameStage.show();

        // attach "game loop" to timeline to play it
        var frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> step(SECOND_DELAY));
        var animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
    }

    private Scene setupLevel() {
        var image = new Image(this.getClass().getClassLoader().getResourceAsStream(BOUNCER_IMAGE));
        myBall = new Ball(image, -1, 1, SCENE_WIDTH, 0);

        image = new Image(this.getClass().getClassLoader().getResourceAsStream(RAFT_IMAGE));
        myRaft = new Raft(image, myBall);

        this.myLevel = getNextLevel();
        this.myGameStatus = new GameStatus(myLevel.getSceneRoot(), livesRemaining, myLevel.getBricksRemaining());

        // respond to input
        myLevel.getScene().setOnKeyPressed(e -> handleKeyInput(e.getCode()));

        return myLevel.getScene();
    }


    // Change properties of shapes to animate them
    private void step(double elapsedTime) {
        myBall.step(elapsedTime, BALL_SPEED, myScene.getWidth(), myScene.getHeight());
        if (myExtraBall!=null) {
            myExtraBall.step(elapsedTime, BALL_SPEED, myScene.getWidth(), myScene.getHeight());
        }
        myRaft.step();
        myLevel.step(elapsedTime, myRaft, myBall);

        //Only for non-extra ball
        if (myBall.getY() > myScene.getHeight()) {
            livesRemaining.set(livesRemaining.get() - 1);
            myBall.resetBall();
        }
        if (myExtraBall!=null && myExtraBall.getY() > myScene.getHeight()) {
            myLevel.setIsExtraBall(false);
            this.myExtraBall = null;
        }

        if (livesRemaining.get() <= 0) {
            this.isGameOver = true;
        }

        if (myLevel.getIsLevelCleared()) {
            this.gameStage.setScene(setupLevel());
        }

        if (myLevel.getIsExtraBall()) {
            addNewBall();
        }
    }

    private List<Level> createLevels() {
        List<Level> myLevels = new ArrayList<>();

        //create level 1
        var backgroundImage = new Image(this.getClass().getClassLoader().getResourceAsStream("level1.jpg"));
        double[] level1Probs = {0.75, 0.2, 0.05};
        Level level1 = new Level(backgroundImage, level1Probs, 0.5);
        myLevels.add(level1);


        //create level 2
        backgroundImage = new Image(this.getClass().getClassLoader().getResourceAsStream("level2.jpg"));
        double[] level2Probs = {0.5, 0.3, 0.2};
        Level level2 = new Level(backgroundImage, level2Probs, 1);
        myLevels.add(level2);


        return myLevels;
    }

    private Level getNextLevel() {

        Level nextLevel = this.myLevelIterator.next();
        nextLevel.getSceneRoot().getChildren().add(myRaft);
        nextLevel.getSceneRoot().getChildren().add(myBall);
        nextLevel.getSceneRoot().setBottomAnchor(myRaft, 0.0);
        nextLevel.initializeBricks(myBall);
        return nextLevel;
    }


    public void handleKeyInput(KeyCode code) {
        myRaft.handleKeyInput(code, myScene.getWidth());
        myBall.handleKeyInput(code);
        myLevel.handleKeyInput(code, myRaft, myBall);
    }

    public void addNewBall() {
        //max 2 balls at once
        if (this.myExtraBall == null) {
            System.out.println("extra");
            var image = new Image(this.getClass().getClassLoader().getResourceAsStream(BOUNCER_IMAGE));
            this.myExtraBall = new Ball(image, -1, 1, SCENE_WIDTH, 0);
            this.myLevel.getSceneRoot().getChildren().add(myExtraBall);
            this.myRaft.addExtraBall(myExtraBall);
            for (GreenhouseGas[] row : this.myLevel.getBricks()) {
                for (GreenhouseGas gas : row) {
                    gas.addExtraBall(this.myExtraBall);
                }
            }
        }
    }

}