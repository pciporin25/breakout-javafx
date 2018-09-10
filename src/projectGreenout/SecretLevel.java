package projectGreenout;

import javafx.beans.property.SimpleLongProperty;
import javafx.scene.image.Image;

import java.util.TreeMap;

public class SecretLevel extends Level {

    public SecretLevel(Image backgroundImage,
                       TreeMap<String, Double> brickProbs,
                       double powerupProb,
                       double raftScale) {
        super(backgroundImage, brickProbs, powerupProb, raftScale);
    }

    @Override
    public void initializeBricks(Ball levelBall) {
        GreenhouseGas toPopulate[][] = new GreenhouseGas[3][8];
        long numBricks = 0;
        var minProb = getGasProbabilities().pollFirstEntry();
        var secondSmallest = getGasProbabilities().pollFirstEntry();
        var largest = getGasProbabilities().pollLastEntry();

        for (int row=0; row<toPopulate.length; row++) {
            for (int col=0; col<toPopulate[row].length; col++) {
                double brickProb = Math.random();
                GreenhouseGas toAdd;

                if (brickProb <= minProb.getValue()) {
                    toAdd = getGas(minProb, levelBall);
                    getSceneRoot().getChildren().add(toAdd);
                    toPopulate[row][col] = toAdd;
                }

                else if (brickProb <= secondSmallest.getValue()) {
                    toAdd = getGas(secondSmallest, levelBall);
                    getSceneRoot().getChildren().add(toAdd);
                    toPopulate[row][col] = toAdd;
                }
                else {
                    toAdd = getGas(largest, levelBall);
                    getSceneRoot().getChildren().add(toAdd);
                    toPopulate[row][col] = toAdd;
                }

                //Align bricks according to position within array
                toAdd.setX(col * toAdd.getLayoutBounds().getWidth());
                toAdd.setY(this.levelScene.getHeight() - ((row + 1) * toAdd.getLayoutBounds().getHeight()));

                //increment brick counter
                numBricks++;
            }
        }
        this.bricksRemaining = new SimpleLongProperty(numBricks);
        this.bricks = toPopulate;
    }

    @Override
    protected void checkBrickCollisions(Raft myRaft, Ball myBall) {
        for (int row=0; row<bricks.length; row++) {
            for (int col=0; col<bricks[row].length; col++) {
                GreenhouseGas brick = bricks[row][col];

                if (brick!=null && brick.checkForBallCollision()) {
                    getSceneRoot().getChildren().remove(brick);
                    bricks[row][col] = null;
                    bricksRemaining.set(bricksRemaining.get() - 1);
                    generatePowerUp(brick.getX(), brick.getY(), -30, myRaft, myBall);
                }
            }
        }
    }

}
