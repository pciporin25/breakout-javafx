package projectGreenout;

import javafx.beans.binding.Bindings;
import javafx.beans.property.LongProperty;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class GameStatus {

    private Text livesRemainingText;
    private Text bricksRemainingText;

    GameStatus(AnchorPane root, LongProperty livesRemaining, LongProperty bricksRemaining) {
        this.livesRemainingText = new Text("Lives Remaining: " + livesRemaining);
        root.getChildren().add(livesRemainingText);
        root.setTopAnchor(livesRemainingText, 10.0);
        root.setRightAnchor(livesRemainingText, 10.0);
        livesRemainingText.textProperty().bind(Bindings.createStringBinding(() -> "Lives Remaining: " + livesRemaining.get(), livesRemaining));

        this.bricksRemainingText = new Text("Bricks Remaining: " + bricksRemaining);
        root.getChildren().add(bricksRemainingText);
        root.setTopAnchor(bricksRemainingText, 10 + livesRemainingText.getLayoutBounds().getHeight());
        root.setRightAnchor(bricksRemainingText, 10.0);
        bricksRemainingText.textProperty().bind(Bindings.createStringBinding(() -> "Bricks Remaining: " + bricksRemaining.get(), bricksRemaining));

        this.livesRemainingText.setFill(Color.RED);
        this.bricksRemainingText.setFill(Color.RED);
    }

}
