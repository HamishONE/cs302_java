package warlords;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;

import java.util.List;

public class GameView {

	private Scene scene;
	private GraphicsContext gc;
	private Game game;
	
	public GameView(Game game) {

		this.game = game;

		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		Canvas canvas = new Canvas(game.getWidth(), game.getHeight());
		grid.add(canvas, 0, 0);
		gc = canvas.getGraphicsContext2D();
		clearCanvas();

		scene = new Scene(grid);
		Main.setScene(scene);
	}

	private void clearCanvas() {
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, game.getWidth(), game.getHeight());
	}

	public void drawObjects(List<GameObject> gameObjects) {
		clearCanvas();
		for (GameObject gameObject : gameObjects) {
			gc.save(); // saves the current state on stack, including the current transform
			Rotate r = new Rotate(gameObject.getRotation()*(180/Math.PI), gameObject.getXPos(), gameObject.getYPos());
			gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
			gc.drawImage(gameObject.getSprite(), gameObject.getXPos(), gameObject.getYPos());
			gc.restore(); // back to original state (before rotation)

			//http://stackoverflow.com/questions/18260421/how-to-draw-image-rotated-on-javafx-canvas
			//TODO need to make sure that any sprites are pivoting from center of sprite, not point where they are being placed
			//TODO need to talk to Hummush on where we want the origin of sprites to be...

		}
	}

	public Scene getScene() {
		return scene;
	}
}
