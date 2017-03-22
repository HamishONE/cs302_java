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

	private static final int GAME_HEIGHT = 450;
	private static final int GAME_WIDTH = 1200;

	private Scene scene;
	private GraphicsContext gc;

	public GameView() {

		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		Canvas canvas = new Canvas(GAME_WIDTH, GAME_HEIGHT);
		grid.add(canvas, 0, 0);
		gc = canvas.getGraphicsContext2D();
		clearCanvas();

		scene = new Scene(grid);
		Main.setScene(scene);
	}

	private void clearCanvas() {
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
	}

	public void drawObjects(List<GameObject> gameObjects) {
		clearCanvas();
		for (GameObject gameObject : gameObjects) {
			gc.setFill(Color.GREEN);
			gc.fillOval(gameObject.getXPos(), gameObject.getYPos(), 20, 20);
		}
	}

	public Scene getScene() {
		return scene;
	}
}
