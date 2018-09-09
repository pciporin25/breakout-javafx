package projectGreenout;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import java.lang.Math;

public class Level {

    public static final int SCENE_SIZE = 500;

    private AnchorPane sceneRoot;
    private Scene levelScene;

    private GreenhouseGas[][] bricks;
    private double co2prob;
    private double ch4prob;
    private double n2oprob;

    private double satelliteProb;
    private double powerupProb;


    public Level(AnchorPane root, Image myBackgroundImage, Ball ball, Raft raft, double brickProbs[]) {
        //Set up scene root as global variable for this level
        this.sceneRoot = root;

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

        //Generate scene
        this.levelScene = setupLevel(myBackgroundImage, ball, raft);
    }

    private Scene setupLevel(Image myBackgroundImage, Ball ball, Raft raft) {
        sceneRoot.getChildren().add(ball);
        sceneRoot.getChildren().add(raft);
        sceneRoot.setBottomAnchor(raft, 0.0);

        ImagePattern backgroundImage = new ImagePattern(myBackgroundImage);
        return new Scene(sceneRoot, SCENE_SIZE, SCENE_SIZE, backgroundImage);
    }

    private GreenhouseGas[][] initializeBricks() {
        GreenhouseGas toPopulate[][] = new GreenhouseGas[3][8];

        for (int row=0; row<toPopulate.length; row++) {
            for (int col=0; col<toPopulate[row].length; col++) {
                double brickProb = Math.random();
                GreenhouseGas toAdd;

                if (brickProb > co2prob) {
                    toAdd = new CO2();
                    sceneRoot.getChildren().add(toAdd);
                    toPopulate[row][col] = toAdd;
                }
                else if (brickProb > n2oprob) {
                    toAdd = new N2O();
                    sceneRoot.getChildren().add(toAdd);
                    toPopulate[row][col] = toAdd;
                }
                else {
                    toAdd = new CH4();
                    sceneRoot.getChildren().add(toAdd);
                    toPopulate[row][col] = toAdd;
                }

                //Align bricks according to position within array
                toAdd.setX(col * toAdd.getLayoutBounds().getWidth());
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

}
