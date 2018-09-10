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
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.security.Key;
import java.util.*;

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
    public static final int RAFT_SPEED = 20;
    public static final int POWER_UP_SPEED = 30;

    private Pane splashLayout;
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
    private boolean isGameOver = false;
    private boolean startedSecretLevel = false;

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void init() {
        //Load splash view for instructions
        ImageView splashView = new ImageView(new Image(this.getClass().getClassLoader().getResourceAsStream("splash.jpeg")));
        this.splashLayout = new VBox();
        splashLayout.getChildren().add(splashView);
    }

    public void start(Stage myStage)
    {
        this.gameStage = myStage;
        this.myLevels = createLevels();
        this.myLevelIterator = this.myLevels.iterator();
        myScene = new Scene(splashLayout);

        gameStage.setTitle("Project GREENOUT: Greenhouse Gas Elimination");
        gameStage.setScene(myScene);
        gameStage.show();

        //Set listener to start the game once any button is pressed on splash view
        myScene.setOnKeyPressed(e -> startGame());
    }



    private List<Level> createLevels() {
        List<Level> myLevels = new ArrayList<>();

        //create level 1
        var backgroundImage = new Image(this.getClass().getClassLoader().getResourceAsStream("level1.jpg"));
        TreeMap<String, Double> level1Probs = generateMap(0.8, 0.15, 0.05);
        Level level1 = new Level(backgroundImage, level1Probs, 0.25, 1);
        myLevels.add(level1);

        //create level 2
        backgroundImage = new Image(this.getClass().getClassLoader().getResourceAsStream("level2.jpg"));
        TreeMap<String, Double> level2Probs = generateMap(0.6, 0.2, 0.2);
        Level level2 = new Level(backgroundImage, level2Probs, 0.4, 0.9);
        myLevels.add(level2);

        //create level 3
        backgroundImage = new Image(this.getClass().getClassLoader().getResourceAsStream("level3.jpg"));
        TreeMap<String, Double> level3Probs = generateMap(0.3, 0.4, 0.3);
        Level level3 = new Level(backgroundImage, level3Probs, 0.7, 0.8);
        myLevels.add(level3);

        //create level 4
        backgroundImage = new Image(this.getClass().getClassLoader().getResourceAsStream("level4.jpg"));
        TreeMap<String, Double> level4Probs = generateMap(0.25, 0.35, 0.4);
        Level level4 = new Level(backgroundImage, level4Probs, 0.8, 0.7);
        myLevels.add(level4);

        //create level 5
        backgroundImage = new Image(this.getClass().getClassLoader().getResourceAsStream("level5.jpg"));
        TreeMap<String, Double> level5Probs = generateMap(0.1, 0.4, 0.5);
        Level level5 = new Level(backgroundImage, level5Probs, 1, 0.6);
        myLevels.add(level5);

        return myLevels;
    }

    private TreeMap<String, Double> generateMap(double co2, double n2o, double ch4) {
        TreeMap<String, Double> myMap = new TreeMap<>();
        myMap.put("co2", co2);
        myMap.put("n2o", n2o);
        myMap.put("ch4", ch4);
        return myMap;
    }




    private void startGame() {
        //load first level
        this.myScene = setupLevel();
        gameStage.setScene(myScene);
        gameStage.show();

        // attach "game loop" to timeline to play it
        var frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> step(SECOND_DELAY));
        var animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
    }

    //this method loads the next level of the game, returning the corresponding Scene
    private Scene setupLevel() {
        var image = new Image(this.getClass().getClassLoader().getResourceAsStream(BOUNCER_IMAGE));
        myBall = new Ball(image, -1, 1, SCENE_WIDTH, 0, isGameOver);

        image = new Image(this.getClass().getClassLoader().getResourceAsStream(RAFT_IMAGE));
        myRaft = new Raft(image, myBall);

        this.myLevel = getNextLevel();
        if (!isGameOver) {
            this.myGameStatus = new GameStatus(myLevel.getSceneRoot(), livesRemaining, myLevel.getBricksRemaining());
        }

        // respond to input
        myLevel.getScene().setOnKeyPressed(e -> handleKeyInput(e.getCode()));

        return myLevel.getScene();
    }


    // Called at each frame of game
    private void step(double elapsedTime) {
        //At start of each frame, check if no lives left or if game already complete
        if (livesRemaining.get() <= 0 || this.isGameOver) {
            this.isGameOver = true;
            if (!startedSecretLevel) {
                gameOver();
            }
            this.startedSecretLevel = true;
        }
        //If game is not yet over but level is cleared, load next level
        else if (myLevel.getIsLevelCleared()) {
            this.gameStage.setScene(setupLevel());
        }

        //call methods for child objects
        myBall.step(elapsedTime, BALL_SPEED, myScene.getWidth(), myScene.getHeight());
        myRaft.step();
        myLevel.step(elapsedTime, myRaft, myBall);

        //if the Level class indicates that extra ball should be added, call private method
        if (myLevel.getIsExtraBall()) {
            addNewBall();
        }
        //if extra ball power-up enabled, run step method for this object too
        if (myExtraBall!=null) {
            myExtraBall.step(elapsedTime, BALL_SPEED, myScene.getWidth(), myScene.getHeight());
        }
        //when extra ball exits scene, reset global variables
        if (myExtraBall!=null && myExtraBall.getIsOutOfPlay()) {
            myLevel.setIsExtraBall(false);
            myLevel.getSceneRoot().getChildren().remove(myExtraBall);
            this.myExtraBall = null;
        }

        //When non-extra ball exits scene, decrease number of lives remaining
        if (myBall.getY() >= myScene.getHeight()) {
            livesRemaining.set(livesRemaining.get() - 1);
        }


    }




    private Level getNextLevel() {
        //check if game is over.  if so, load secret level (without attaching game objects yet)
        if (!this.myLevelIterator.hasNext() || this.isGameOver) {
            this.isGameOver = true;

            var backgroundImage = new Image(this.getClass().getClassLoader().getResourceAsStream("secret-level.jpg"));
            TreeMap<String, Double> secretLevelProbs = generateMap(0.4, 0.4, 0.2);
            Level secretLevel = new SecretLevel(backgroundImage, secretLevelProbs, 1, 1.0);

            return secretLevel;
        }

        Level nextLevel = this.myLevelIterator.next();
        myRaft.setScaleX(nextLevel.getRaftScale());
        myRaft.setScaleY(nextLevel.getRaftScale());
        nextLevel.getSceneRoot().getChildren().add(myRaft);
        nextLevel.getSceneRoot().getChildren().add(myBall);
        nextLevel.getSceneRoot().setBottomAnchor(myRaft, 0.0);
        nextLevel.initializeBricks(myBall);
        return nextLevel;
    }

    private void gameOver() {
        this.gameStage.setScene(setupLevel());
        this.myLevel.getScene().setOnMouseClicked(e -> showSecretLevel());
    }

    private void showSecretLevel() {
        myLevel.getSceneRoot().getChildren().add(myRaft);
        myLevel.getSceneRoot().getChildren().add(myBall);
        myLevel.getSceneRoot().setTopAnchor(myRaft, 0.0);
        myLevel.initializeBricks(myBall);
    }






    private void handleKeyInput(KeyCode code) {
        myRaft.handleKeyInput(code, myScene.getWidth());
        myBall.handleKeyInput(code);
        myLevel.handleKeyInput(code, myRaft, myBall);

        //CHEAT KEYS
        //more lives
        if (code == KeyCode.L) {
            livesRemaining.set(livesRemaining.get() + 1);
        }
        //secret level
        else if (code == KeyCode.S) {
            this.isGameOver = true;
        }
        //all blocks destroy with one hit
        else if (code == KeyCode.A) {
            for (int i=0; i<this.myLevel.getBricks().length; i++) {
                for (int j=0; j<this.myLevel.getBricks()[i].length; j++) {
                   this.myLevel.getBricks()[i][j].oneHitRemaining();
                }
            }
        }
    }

    private void addNewBall() {
        //max 2 balls at once
        if (this.myExtraBall == null) {
            System.out.println("extra");
            var image = new Image(this.getClass().getClassLoader().getResourceAsStream(BOUNCER_IMAGE));
            this.myExtraBall = new Ball(image, -1, 1, SCENE_WIDTH, 0, isGameOver);
            this.myLevel.getSceneRoot().getChildren().add(myExtraBall);
            this.myRaft.addExtraBall(myExtraBall);

            for (int i=0; i<this.myLevel.getBricks().length; i++) {
                for (int j=0; j<this.myLevel.getBricks()[i].length; j++) {
                    GreenhouseGas gas = this.myLevel.getBricks()[i][j];
                    System.out.println("gas is " + gas);
                    System.out.println("extra ball is " + this.myExtraBall);
                    gas.addExtraBall(this.myExtraBall);
                }
            }
        }
    }

}