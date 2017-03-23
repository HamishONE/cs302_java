package warlords;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
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
			gc.setFill(Color.GREEN);
			gc.fillOval(gameObject.getXPos()-10, gameObject.getYPos()-10, 20, 20);
		}
	}

	public Scene getScene() {
		return scene;
	}
}
