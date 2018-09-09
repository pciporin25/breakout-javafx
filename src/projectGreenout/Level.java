package projectGreenout;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import java.lang.Math;

public class Level {

    public static final int SCENE_SIZE = 500;

    private ImagePattern backgroundImage;
    private Group sceneRoute
    private Scene levelScene;

    private GreenhouseGas[][] bricks;
    private double co2prob;
    private double ch4prob;
    private double n2oprob;

    private double satelliteProb;
    private double powerupProb;


    public Level(Group root, Image myBackgroundImage, double brickProbs[]) {
        try {
            this.co2prob = brickProbs[0];
            this.ch4prob = brickProbs[1];
            this.n2oprob = brickProbs[2];
        }
        catch (Exception e) {
            System.err.println("Caught exception while loading brick probabilities: " + e.getMessage());
        }

        this.sceneRoute = root;
        this.backgroundImage = new ImagePattern(myBackgroundImage);
        this.bricks = initializeBricks()
        this.levelScene = new Scene(sceneRoute, SCENE_SIZE, SCENE_SIZE, backgroundImage);
    }

    private GreenhouseGas[][] initializeBricks() {
        GreenhouseGas toPopulate[][] = new GreenhouseGas[3][8];

        for (int row=0; row<toPopulate.length; row++) {
            for (int col=0; col<toPopulate[row].length; col++) {
                double brickProb = Math.random();

                if (brickProb > co2prob) {
                    GreenhouseGas toAdd = new CO2();
                    sceneRoute.getChildren().add(toAdd);
                    toPopulate[row][col] = toAdd;
                }
                else if (brickProb > n2oprob) {
                    GreenhouseGas toAdd = new N2O();
                    sceneRoute.getChildren().add(toAdd);
                    toPopulate[row][col] = toAdd;
                }
                else {
                    GreenhouseGas toAdd = new CH4();
                    sceneRoute.getChildren().add(toAdd);
                    toPopulate[row][col] = toAdd;
                }
            }
        }

        return toPopulate;
    }

}
