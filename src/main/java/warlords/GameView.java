package warlords;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

public class GameView {

	private Scene scene;
	private GraphicsContext gc;

	public GameView() {

		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		Canvas canvas = new Canvas(800, 600);
		grid.add(canvas, 0, 0);
		gc = canvas.getGraphicsContext2D();
		clearCanvas();

		scene = new Scene(grid);
		Main.setScene(scene);
	}

	private void clearCanvas() {
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, 800, 600);
	}

	public Scene getScene() {
		return scene;
	}
}
