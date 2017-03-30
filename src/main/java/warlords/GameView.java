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

		//Creates alignment grid and creates canvas within, also creates master graphics context
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		Canvas canvas = new Canvas(width, height);
		grid.add(canvas, 0, 0);
		gc = canvas.getGraphicsContext2D();
		clearCanvas();

		//Creates new master scene
		scene = new Scene(grid);
		Main.setScene(scene);
	}

	/**
	 * Helper function to make the canvas totally black
	 */
	private void clearCanvas() {
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, width, height);
	}

	/**
	 *Draws the provided objects to the canvas
	 *
	 * @param gameObjects ArrayList of all the game objects to be rendered on the screen
	 */
	public void drawObjects(List<GameObject> gameObjects) {

		if (!Main.isDebugMode()) {
			clearCanvas();
		}

		//Loop through each object, rendering as they go
		for (GameObject gameObject : gameObjects) {

			gc.save(); // saves the current state on stack, including the current transform

			//Render dead warlords as transparent
			if(gameObject instanceof Warlord && ((Warlord) gameObject).isDead()) {
				gc.setGlobalAlpha(0.4);
			}

			//Rotate entire canvas to adjust for object rotation
			Rotate r = new Rotate(gameObject.getRotation()*(180/Math.PI), gameObject.getXPos(), gameObject.getYPos());
			gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());

			//Place objects source image at specified location on the canvas
			Image image = getImage(gameObject.getSpritePath(), gameObject.getWidth(), gameObject.getHeight());
			double x = gameObject.getXPos() - gameObject.getWidth()/2;
			double y = gameObject.getYPos() - gameObject.getHeight()/2;
			gc.drawImage(image, x, y);

			if (Main.isDebugMode()) {
				gc.setStroke(Color.RED);
				gc.strokeRect(x, y, gameObject.getWidth(), gameObject.getHeight());
			}

			gc.restore(); // back to original state (before rotation)

		}
	}

	/**
	 * Draws menu buttons to canvas
	 *
	 * @param menuItems list of menu items to be added
	 */
	public void drawMenuItems(List<MenuItem> menuItems) {

		//Constants for placements of menu items
		int PADDING = 50;
		int TEXT_HEIGHT = 50;
		int BORDER_WIDTH = 4;
		int CORNER_RADIUS = 15;


		clearCanvas();
		double maxWidth = width - PADDING*2;
		double y = PADDING + TEXT_HEIGHT/2;

		//loop through all the menu items and render each
		for (MenuItem menuItem : menuItems) {

			//If the current item is selected, add a border to the outside
			if (menuItem.isSelected()) {
				gc.setFill(Color.WHITE);
				gc.fillRoundRect(PADDING - BORDER_WIDTH, y - TEXT_HEIGHT/2 - 10 - BORDER_WIDTH, maxWidth + BORDER_WIDTH*2,
						TEXT_HEIGHT + 20 + BORDER_WIDTH*2, CORNER_RADIUS, CORNER_RADIUS);
			}

			//Set colour and fill buttons with it
			gc.setFill(Color.ORANGE);
			gc.fillRoundRect(PADDING, y - TEXT_HEIGHT/2 - 10, maxWidth, TEXT_HEIGHT + 20, CORNER_RADIUS, CORNER_RADIUS);

			//Write text to the button
			gc.setFill(Color.WHITE);
			gc.setFont(new Font("Algerian", TEXT_HEIGHT));
			gc.setTextAlign(TextAlignment.CENTER);
			gc.setTextBaseline(VPos.CENTER);
			gc.fillText(menuItem.getText(), width/2, y, maxWidth);

			y += PADDING*2;
		}
	}

	/**
	 * Draws the pause text on the screen
	 */
	public void drawPauseIndicator() {
		//Sets font to white and draws "Pause", centered at the top of the screen
		gc.setFill(Color.WHITE);
		gc.setFont(new Font("Corbel", 50));
		gc.setTextAlign(TextAlignment.CENTER);
		gc.setTextBaseline(VPos.CENTER);
		gc.fillText("Paused", width/2, 50, width - 100);
	}

	/**
	 * Draws the time remaining to the screen
	 *
	 * @param secsRemaining input of number of seconds remaining of gameplay
	 */
	public void drawTimer(int secsRemaining) {
		//Make text from seconds to show mins and seconds, write at top of screen
		String text = String.format("%d:%02d", secsRemaining/60, secsRemaining%60);
		gc.setFill(Color.WHITE);
		gc.setFont(new Font("Cambria", 30));
		gc.fillText(text, width - 250, 30);
	}

	/**
	 * Helper function to get an image, this gets the image path from a cached Image instance, or if it does not already exist, creates it.
	 *
	 * @param path		Path to image location
	 * @param width		Width of image
	 * @param height	Height of image
	 * @return			instance of Image with the requested image within
	 */
	private Image getImage(String path, double width, double height) {
		return imageCache.computeIfAbsent(path, p -> new Image(getClass().getResource(p).toString(), width, height, false, true, true));
	}

	/**
	 * Gets the current Scene attached to the view
	 *
	 * @return the scene for the view
	 */
	public Scene getScene() {
		return scene;
	}
}
