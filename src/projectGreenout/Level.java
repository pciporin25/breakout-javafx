package projectGreenout;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;

import java.lang.Math;
import java.util.Map;
import java.util.TreeMap;

import static projectGreenout.BreakoutGameManager.SCENE_WIDTH;

public class Level {

    private AnchorPane sceneRoot;
    public Scene levelScene;
    protected GreenhouseGas[][] bricks;
    private TreeMap<String, Double> gasProbabilities;
    protected LongProperty bricksRemaining;
    private double satelliteProb;
    private double powerupProb;
    private PowerUp activePowerUp;
    private double raftScale;

    private boolean isExtraBall = false;
    private boolean isLevelCleared = false;


    public Level(Image backgroundImage,
                 TreeMap<String, Double> brickProbs,
                 double powerupProb,
                 double raftScale) {

        //Set up scene before generating bricks
        this.sceneRoot = new AnchorPane();
        this.levelScene = new Scene(sceneRoot, SCENE_WIDTH, BreakoutGameManager.SCENE_HEIGHT, new ImagePattern(backgroundImage));
        this.powerupProb = powerupProb;
        this.raftScale = raftScale;

        this.gasProbabilities = brickProbs;
    }


    public void initializeBricks(Ball levelBall) {
        GreenhouseGas toPopulate[][] = new GreenhouseGas[3][8];
        long numBricks = 0;
        var minProb = this.gasProbabilities.pollFirstEntry();
        var secondSmallest = this.gasProbabilities.pollFirstEntry();
        var largest = this.gasProbabilities.pollLastEntry();

        for (int row=0; row<toPopulate.length; row++) {
            for (int col=0; col<toPopulate[row].length; col++) {
                double brickProb = Math.random();
                GreenhouseGas toAdd;

                if (brickProb <= minProb.getValue()) {
                    toAdd = getGas(minProb, levelBall);
                    sceneRoot.getChildren().add(toAdd);
                    toPopulate[row][col] = toAdd;
                }

                else if (brickProb <= secondSmallest.getValue()) {
                    toAdd = getGas(secondSmallest, levelBall);
                    sceneRoot.getChildren().add(toAdd);
                    toPopulate[row][col] = toAdd;
                }
                else {
                    toAdd = getGas(largest, levelBall);
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

    protected GreenhouseGas getGas(Map.Entry<String, Double> type, Ball myBall) {
        switch (type.getKey()) {
            case "co2":
                return new CO2(myBall);
            case "n2o":
                return new N2O(myBall);
            case "ch4":
            default:
                return new CH4(myBall);
        }
    }



    public void step(double elapsedTime, Raft myRaft, Ball myBall) {
        //level is responsible for monitoring collisions with bricks, generating power-ups
        checkBrickCollisions(myRaft, myBall);

        //monitor any active power-ups and destroy them if necessary
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

    protected void checkBrickCollisions(Raft myRaft, Ball myBall) {
        for (int row=0; row<bricks.length; row++) {
            for (int col=0; col<bricks[row].length; col++) {
                GreenhouseGas brick = bricks[row][col];

                if (brick!=null && brick.checkForBallCollision()) {
                    sceneRoot.getChildren().remove(brick);
                    bricks[row][col] = null;
                    bricksRemaining.set(bricksRemaining.get() - 1);

                    //once brick has been destroyed, generate power-up (at random)
                    generatePowerUp(brick.getX(), brick.getY(), 30, myRaft, myBall);
                }
            }
        }
    }

    protected void generatePowerUp(double startX, double startY, double speedY, Raft myRaft, Ball myBall) {
        //Only generate the power-up if none are already active and probability is satisfied
        if (activePowerUp==null && Math.random() < powerupProb) {
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
            generatePowerUp(bricks[1][1].getX(), bricks[1][1].getY(), BreakoutGameManager.POWER_UP_SPEED, myRaft, myBall);
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



    //Getters and setters

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

    public double getRaftScale() {
        return this.raftScale;
    }

    public TreeMap<String, Double> getGasProbabilities() {
        return this.gasProbabilities;
    }

}
