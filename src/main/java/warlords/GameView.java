package warlords;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Rotate;
import java.util.HashMap;
import java.util.List;

/**
 * Create a canvas and draws various game elements onto it.
 */
public class GameView {

	private Scene scene;
	private GraphicsContext gc;
	private HashMap<String, Image> imageCache = new HashMap<>();
	private double width;
	private double height;

	/**
	 * Create a new canvas of the specified dimensions
	 * @param width width of the canvas
	 * @param height height of the canvas
	 */
	public GameView(double width, double height) {

		this.width = width;
		this.height = height;

		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		Canvas canvas = new Canvas(width, height);
		grid.add(canvas, 0, 0);
		gc = canvas.getGraphicsContext2D();
		clearCanvas();

		scene = new Scene(grid);
		Main.setScene(scene);
	}

	private void clearCanvas() {
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, width, height);
	}

	public void drawObjects(List<GameObject> gameObjects) {
		clearCanvas();

		for (GameObject gameObject : gameObjects) {

			gc.save(); // saves the current state on stack, including the current transform
			if(gameObject instanceof Warlord && ((Warlord) gameObject).isDead()) {
				gc.setGlobalAlpha(0.4);
			}

			Rotate r = new Rotate(gameObject.getRotation()*(180/Math.PI), gameObject.getXPos(), gameObject.getYPos());
			gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());

			Image image = getImage(gameObject.getSpritePath(), gameObject.getWidth(), gameObject.getHeight());
			double x = gameObject.getXPos() - gameObject.getWidth()/2;
			double y = gameObject.getYPos() - gameObject.getHeight()/2;
			gc.drawImage(image, x, y);

			if (System.getProperty("debug") != null && System.getProperty("debug").equalsIgnoreCase("true")) {
				gc.setStroke(Color.RED);
				gc.strokeRect(x, y, gameObject.getWidth(), gameObject.getHeight());
			}

			gc.restore(); // back to original state (before rotation)

		}
	}

	public void drawMenuItems(List<MenuItem> menuItems) {

		int PADDING = 50;
		int TEXT_HEIGHT = 50;
		int BORDER_WIDTH = 4;
		int CORNER_RADIUS = 15;

		clearCanvas();
		double maxWidth = width - PADDING*2;

		double y = PADDING + TEXT_HEIGHT/2;
		for (MenuItem menuItem : menuItems) {

			if (menuItem.isSelected()) {
				gc.setFill(Color.WHITE);
				gc.fillRoundRect(PADDING - BORDER_WIDTH, y - TEXT_HEIGHT/2 - 10 - BORDER_WIDTH, maxWidth + BORDER_WIDTH*2,
						TEXT_HEIGHT + 20 + BORDER_WIDTH*2, CORNER_RADIUS, CORNER_RADIUS);
			}

			gc.setFill(Color.ORANGE);
			gc.fillRoundRect(PADDING, y - TEXT_HEIGHT/2 - 10, maxWidth, TEXT_HEIGHT + 20, CORNER_RADIUS, CORNER_RADIUS);

			gc.setFill(Color.WHITE);
			gc.setFont(new Font("Algerian", TEXT_HEIGHT));
			gc.setTextAlign(TextAlignment.CENTER);
			gc.setTextBaseline(VPos.CENTER);
			gc.fillText(menuItem.getText(), width/2, y, maxWidth);

			y += PADDING*2;
		}
	}

	public void drawPauseIndicator() {
		gc.setFill(Color.WHITE);
		gc.setFont(new Font("Corbel", 50));
		gc.setTextAlign(TextAlignment.CENTER);
		gc.setTextBaseline(VPos.CENTER);
		gc.fillText("Paused", width/2, 50, width - 100);
	}

	public void drawTimer(int secsRemaining) {
		String text = String.format("%d:%02d", secsRemaining/60, secsRemaining%60);
		gc.setFill(Color.WHITE);
		gc.setFont(new Font("Cambria", 30));
		gc.fillText(text, width - 250, 30);
	}

	private Image getImage(String path, double width, double height) {
		return imageCache.computeIfAbsent(path, p -> new Image(getClass().getResource(p).toString(), width, height, false, true, true));
	}

	public Scene getScene() {
		return scene;
	}
}
