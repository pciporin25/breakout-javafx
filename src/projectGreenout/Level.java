package projectGreenout;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;

import java.lang.Math;

import static projectGreenout.BreakoutGameManager.SCENE_WIDTH;

public class Level {

    private AnchorPane sceneRoot;
    public Scene levelScene;

    private GreenhouseGas[][] bricks;
    private double co2prob;
    private double n2oprob;
    private double ch4prob;

    private LongProperty bricksRemaining;
    private int timeRemaining;

    private double satelliteProb;
    private double powerupProb;
    private PowerUp activePowerUp;

    private boolean isExtraBall = false;
    private boolean isLevelCleared = false;


    public Level(Image backgroundImage,
                 double brickProbs[]) {

        //Set up scene before generating bricks
        setupScene(backgroundImage);

        //Set brick generation probabilities and populate bricks array
        try {
            this.co2prob = brickProbs[0];
            this.ch4prob = brickProbs[1];
            this.n2oprob = brickProbs[2];
        }
        catch (Exception e) {
            System.err.println("Caught exception while loading brick probabilities: " + e.getMessage());
        }
        //this.bricks = initializeBricks();
    }

    private void setupScene(Image backgroundImage) {
        this.sceneRoot = new AnchorPane();

        this.levelScene = new Scene(sceneRoot, SCENE_WIDTH, BreakoutGameManager.SCENE_HEIGHT, new ImagePattern(backgroundImage));
    }


    public void initializeBricks(Ball levelBall) {
        GreenhouseGas toPopulate[][] = new GreenhouseGas[3][8];
        long numBricks = 0;

        for (int row=0; row<toPopulate.length; row++) {
            for (int col=0; col<toPopulate[row].length; col++) {
                double brickProb = Math.random();
                GreenhouseGas toAdd;

                if (brickProb > co2prob) {
                    toAdd = new CO2(levelBall);
                    sceneRoot.getChildren().add(toAdd);
                    toPopulate[row][col] = toAdd;
                }
                else if (brickProb > n2oprob) {
                    toAdd = new N2O(levelBall);
                    sceneRoot.getChildren().add(toAdd);
                    toPopulate[row][col] = toAdd;
                }
                else {
                    toAdd = new CH4(levelBall);
                    sceneRoot.getChildren().add(toAdd);
                    toPopulate[row][col] = toAdd;
                }

                //Align bricks according to position within array
                toAdd.setX(col * toAdd.getLayoutBounds().getWidth());
                toAdd.setY(row * toAdd.getLayoutBounds().getHeight());

                //increment brick counter
                numBricks++;
            }
        }
        this.bricksRemaining = new SimpleLongProperty(numBricks);
        this.bricks = toPopulate;
    }

    public GreenhouseGas[][] getBricks() {
        return this.bricks;
    }

    public LongProperty getBricksRemaining() {
        return this.bricksRemaining;
    }

    public boolean getIsLevelCleared() {
        return this.isLevelCleared;
    }

    public void setIsExtraBall(boolean val) {
        this.isExtraBall = val;
    }

    public boolean getIsExtraBall() {
        return this.isExtraBall;
    }

    public Scene getScene() {
        return this.levelScene;
    }

    public AnchorPane getSceneRoot() {
        return this.sceneRoot;
    }

    public void step(double elapsedTime, Raft myRaft, Ball myBall) {
        checkBrickCollisions(myRaft, myBall);

        if (this.activePowerUp!=null) {
            boolean shouldDestroy = activePowerUp.step(elapsedTime, this.getScene().getHeight());
            if (shouldDestroy) {
                sceneRoot.getChildren().remove(activePowerUp);
                this.activePowerUp = null;
            }
        }

        if (this.bricksRemaining.get() == 0) {
            this.isLevelCleared = true;
        }
    }

    private void checkBrickCollisions(Raft myRaft, Ball myBall) {
        for (int row=0; row<bricks.length; row++) {
            for (int col=0; col<bricks[row].length; col++) {
                GreenhouseGas brick = bricks[row][col];

                if (brick!=null && brick.checkForBallCollision()) {
                    sceneRoot.getChildren().remove(brick);
                    bricks[row][col] = null;
                    bricksRemaining.set(bricksRemaining.get() - 1);
                    generatePowerUp(brick.getX(), brick.getY(), 30, myRaft, myBall);
                }
            }
        }
    }

    private void generatePowerUp(double startX, double startY, double speedY, Raft myRaft, Ball myBall) {
        if (activePowerUp==null) {
            activePowerUp = new PowerUp(startX, startY, speedY, myRaft, myBall, this);
            sceneRoot.getChildren().add(activePowerUp);
        }
    }

    //Level-specific cheat codes
    public void handleKeyInput(KeyCode code, Raft myRaft, Ball myBall) {
        if (code == KeyCode.C) {
            clearLevel();
        }
        if (code == KeyCode.P) {
            generatePowerUp(bricks[1][1].getX(), bricks[1][1].getY(), 30, myRaft, myBall);
        }
    }

    private void clearLevel() {
        for (int row=0; row<bricks.length; row++) {
            for (int col=0; col<bricks[row].length; col++) {
                GreenhouseGas brick = bricks[row][col];
                 sceneRoot.getChildren().remove(brick);
                 bricks[row][col] = null;
            }
        }
        this.bricksRemaining.set(0);
    }

}
