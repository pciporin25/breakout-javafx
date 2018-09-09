package projectGreenout;

import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import java.lang.Math;

public class Level {

    private AnchorPane sceneRoot;
    private Scene levelScene;
    private Ball levelBall;
    private Raft levelRaft;

    private GreenhouseGas[][] bricks;
    private double co2prob;
    private double n2oprob;
    private double ch4prob;

    private int bricksRemaining;
    private int timeRemaining;

    private double satelliteProb;
    private double powerupProb;


    public Level(AnchorPane root,
                 Scene myScene,
                 Ball ball,
                 Raft raft,
                 double brickProbs[]) {

        //Set up scene before generating bricks
        setupScene(root, myScene, ball, raft);

        //Set brick generation probabilities and populate bricks array
        try {
            this.co2prob = brickProbs[0];
            this.ch4prob = brickProbs[1];
            this.n2oprob = brickProbs[2];
        }
        catch (Exception e) {
            System.err.println("Caught exception while loading brick probabilities: " + e.getMessage());
        }
        this.bricks = initializeBricks();
    }

    private void setupScene(AnchorPane root, Scene myScene, Ball ball, Raft raft) {
        this.sceneRoot = root;
        this.levelScene = myScene;
        this.levelBall = ball;
        this.levelRaft = raft;


    }


    private GreenhouseGas[][] initializeBricks() {
        GreenhouseGas toPopulate[][] = new GreenhouseGas[3][8];

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
                toAdd.setX((col + 1) * toAdd.getLayoutBounds().getWidth());
                toAdd.setY(row * toAdd.getLayoutBounds().getHeight());
            }
        }

        return toPopulate;
    }

    public GreenhouseGas[][] getBricks() {
        return this.bricks;
    }

    public Scene getScene() {
        return this.levelScene;
    }

    public void step() {
        checkBrickCollisions();
    }

    private void checkBrickCollisions() {
        for (int row=0; row<bricks.length; row++) {
            for (int col=0; col<bricks[row].length; col++) {
                GreenhouseGas brick = bricks[row][col];

                if (brick!=null && brick.checkForBallCollision()) {
                    sceneRoot.getChildren().remove(brick);
                    bricks[row][col] = null;
                }
            }
        }
    }

}
