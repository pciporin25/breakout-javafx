package projectGreenout;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public class BreakoutGameManager extends Application
{
    public static final String BOUNCER_IMAGE = "ball.gif";

    private Scene myScene;
    private Ball myBall;


    public static void main(String[] args)
    {
        launch(args);
    }

    public void start(Stage myStage)
    {
        myScene = setupGame(400, 400, Color.AZURE);
        myStage.setTitle("Project GREENOUT: Greenhouse Gas Elimination");
        myStage.setScene(myScene);
        myStage.show();
    }

    private Scene setupGame(int width, int height, Paint background) {
        // create one top level collection to organize the things in the scene
        var root = new Group();

        // create a place to see the shapes
        var scene = new Scene(root, width, height, background);

        var image = new Image(this.getClass().getClassLoader().getResourceAsStream(BOUNCER_IMAGE));
        myBall = new Ball(image, 1, 1);

        // order added to the group is the order in which they are drawn
        root.getChildren().add(myBall);

        return scene;
    }
}