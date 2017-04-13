package warlords;

import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Rotate;
import java.util.HashMap;
import java.util.List;

/**
 * Create a canvas and draws various game elements onto it.
 */
public class GameView {

	private static final String fontUrl = GameView.class.getResource("/CALIFB.TTF").toExternalForm();

	private Scene scene;
	private GraphicsContext gc;
	private HashMap<String, Image> imageCache = new HashMap<>();
	private double width;
	private double height;
	private double scalingFactor;
	private Pane pane;

	/**
	 * Set the graphics contents font to our standard font with the given size.
	 * @param size The font size in pt.
	 */
	private void setFont(double size) {
		Font font = Font.loadFont(fontUrl, size);
		gc.setFont(font);
	}

	/**
	 * Create a new canvas of the specified dimensions
	 * @param width width of the canvas
	 * @param height height of the canvas
	 */
	public GameView(double width, double height) {

		this.width = width;
		this.height = height;

		scalingFactor = width/800.0;

		// Create the pane and create a canvas within, also gets the graphics context
		pane = new Pane();
		Canvas canvas = new Canvas(width, height);
		pane.getChildren().add(canvas);

		gc = canvas.getGraphicsContext2D();
		gc.setTextAlign(TextAlignment.CENTER);
		gc.setTextBaseline(VPos.CENTER);
		clearCanvas();

		//Creates new master scene
		scene = new Scene(pane);
		Main.setScene(scene);
	}

	/**
	 * Helper function to make the canvas totally black
	 */
	public void clearCanvas() {
		pane.getChildren().removeIf(node -> !(node instanceof Canvas));
		if (!Main.isDebugMode()) {
			gc.setFill(Color.BLACK);
			gc.fillRect(0, 0, width, height);
		}
	}

	/**
	 *Draws the provided objects to the canvas
	 *
	 * @param gameObjects ArrayList of all the game objects to be rendered on the screen
	 */
	public void drawObjects(List<GameObject> gameObjects) {

		//Loop through each object, rendering as they go
		for (GameObject gameObject : gameObjects) {

			gc.save(); // saves the current state on stack, including the current transform

			//Render dead warlords as transparent
			if(gameObject instanceof Warlord && ((Warlord) gameObject).isDead()) {
				gc.setGlobalAlpha(0.4);
			}

			//Rotate entire canvas to adjust for object rotation
			Rotate r = new Rotate(gameObject.getRotation()*(180/Math.PI), gameObject.getXPos()*scalingFactor, gameObject.getYPos()*scalingFactor);
			gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());

			//Place objects source image at specified location on the canvas
			Image image = getImage(gameObject.getSpritePath(), gameObject.getWidth()*scalingFactor, gameObject.getHeight()*scalingFactor);
			double x = gameObject.getXPos() - gameObject.getWidth()/2;
			double y = gameObject.getYPos() - gameObject.getHeight()/2;

			if(gameObject instanceof Wall || gameObject instanceof Ball) {
				ColorAdjust colourChange = new ColorAdjust();
				if(gameObject.getPowerUp() != null) {
					colourChange.setSaturation(0.3);
					/*
						Hue values to get colours
						0.0 = Red
						0.5 = Yellow/Green?
						0.8 = Green
						1.0 = Blue
					 */
					switch (gameObject.getPowerUp()) {
						case PADDLE_FASTER:
							colourChange.setHue(0.0);
							break;
						case PADDLE_SLOWER:
							colourChange.setHue(1.0);
							break;
						case PADDLE_GROW:
							colourChange.setHue(0.8);
							break;
						case PADDLE_SHRINK:
							colourChange.setHue(0.0);
							break;
						case BALL_FASTER:
							colourChange.setHue(1.0);
							break;
						case BALL_SLOWER:
							colourChange.setHue(0.5);
							break;
					}
				}
				gc.setEffect(colourChange);
			}
			gc.drawImage(image, x*scalingFactor, y*scalingFactor);

			if (Main.isDebugMode()) {
				gc.setStroke(Color.RED);
				gc.strokeRect(x*scalingFactor, y*scalingFactor, gameObject.getWidth()*scalingFactor, gameObject.getHeight()*scalingFactor);
			}

			gc.restore(); // back to original state (before rotation and hue change)
		}
	}

	/**
	 * Draws menu buttons to canvas
	 *
	 * @param menuItems list of menu items to be added
	 * @param title 	The title of the menu being drawn
	 */
	private void drawMenuItems(List<MenuItem> menuItems, String title) {

		//Constants for placements of menu items
		int PADDING = 5;
		int TEXT_HEIGHT = 50;
		double WIDTH = 500;
		double HEIGHT = WIDTH*0.22;
		String imgPath;

		double maxWidth = WIDTH - PADDING*2;
		double y = PADDING;

		gc.save();

		//If there is a title, apply it with a drop shadow and shift GC down
		if (title != null) {

			DropShadow dropShadow = new DropShadow();
			dropShadow.setOffsetY(3);
			dropShadow.setWidth(100);
			dropShadow.setColor(Color.BLACK);

			gc.setEffect(dropShadow);
			gc.setFill(Color.WHITE);
			setFont(80*scalingFactor);
			gc.setTextAlign(TextAlignment.CENTER);
			gc.setTextBaseline(VPos.CENTER);
			gc.fillText(title, WIDTH/2*scalingFactor, (y+HEIGHT/2 + PADDING)*scalingFactor, maxWidth*scalingFactor);
			gc.translate(0, 100*scalingFactor);
			gc.setEffect(null);
		}

		//loop through all the menu items and render each
		for (MenuItem menuItem : menuItems) {

			//If the current item is selected, add a border to the outside
			if (menuItem.isSelected()) {
				imgPath = "/buttonSelected.png";
			}
			else {
				imgPath = "/buttonBackground.png";
			}

			Image image = getImage(imgPath, maxWidth*scalingFactor, HEIGHT*scalingFactor);
			gc.drawImage(image, PADDING*scalingFactor, y*scalingFactor);

			//Write text to the button
			gc.setFill(Color.WHITE);
			setFont(TEXT_HEIGHT*scalingFactor);
			gc.setTextAlign(TextAlignment.CENTER);
			gc.setTextBaseline(VPos.CENTER);
			gc.fillText(menuItem.getText(), WIDTH/2*scalingFactor, (y+HEIGHT/2 + PADDING)*scalingFactor, maxWidth*scalingFactor);

			y += HEIGHT+PADDING/2;
		}
		gc.restore();
	}

	/**
	 * Draws two menus to the screen translated to give and animation effect.
	 * @param leftMenu The menu on the left.
	 * @param mainMenu The menu in the centre.
	 * @param rightMenu The menu on the right.
	 * @param rightShowing The portion of the right hand menu that is showing (0-1).
	 */
	public void drawAnimatedMenu(Menu leftMenu, Menu mainMenu, Menu rightMenu, double rightShowing) {

		//Set background of the menus
		clearCanvas();
		Image image = getImage(MenuController.menuBackgroundPath, Game.backendWidth*scalingFactor, Game.backendHeight*scalingFactor);
		double x = 0;
		double y = 0;
		gc.drawImage(image, x*scalingFactor, y*scalingFactor);

		gc.save();
		gc.save();
		gc.save();

		// Draw the left hand menu
		gc.translate((-500 - rightShowing*600)*scalingFactor, 0);
		gc.setEffect(new ColorAdjust(0, 0, -0.5 - rightShowing/2, 0));
		drawMenuItems(leftMenu.getMenuItems(), leftMenu.getTitle());
		gc.restore();

		// Draw the main menu
		gc.translate((100 - rightShowing*600)*scalingFactor, 0);
		gc.setEffect(new ColorAdjust(0, 0, -Math.abs(rightShowing)/2, 0));
		drawMenuItems(mainMenu.getMenuItems(), mainMenu.getTitle());
		gc.restore();

		// Draw the right hand menu
		gc.translate((700 - rightShowing*600)*scalingFactor, 0);
		gc.setEffect(new ColorAdjust(0, 0, -0.5 + rightShowing/2, 0));
		drawMenuItems(rightMenu.getMenuItems(), rightMenu.getTitle());
		gc.restore();
	}

	/**
	 * Draws the pause text on the screen
	 */
	public void drawPauseIndicator() {
		//Sets font to white and draws "Pause", centered at the top of the screen
		gc.setFill(Color.WHITE);
		setFont(50*scalingFactor);
		gc.setTextAlign(TextAlignment.CENTER);
		gc.setTextBaseline(VPos.CENTER);
		gc.fillText("Paused", (Game.backendWidth/2)*scalingFactor, 50, Game.backendWidth - 100);
	}

	/**
	 * Draw a message to the screen asking the user if they are sure they want to exit the game.
	 */
	public void drawExitConfirm() {
		drawOverlay();
		//Sets font to white and draws "Pause", centered at the top of the screen
		gc.setFill(Color.BLACK);
		setFont(50*scalingFactor);
		gc.setTextAlign(TextAlignment.CENTER);
		gc.setTextBaseline(VPos.CENTER);
		gc.fillText("PRESS ESC TO QUIT OR ENTER TO RESUME", (Game.backendWidth/2)*scalingFactor,
				(Game.backendWidth/2)*scalingFactor, Game.backendWidth - 100);
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
		setFont(30*scalingFactor);
		gc.setTextBaseline(VPos.CENTER);
		gc.fillText(text, (Game.backendWidth - 250)*scalingFactor, 30);
	}

	/**
	 * Draws the seconds remaining in the  countdown timer to the screen.
	 * @param secsRemaining input of number of seconds remaining in countdown
	 */
	public void drawCountdown(int secsRemaining) {

		gc.save();

		DropShadow dropShadow = new DropShadow();
		dropShadow.setOffsetY(3);
		dropShadow.setWidth(120);
		dropShadow.setColor(Color.WHITE);

		gc.setEffect(dropShadow);
		gc.setFill(Color.BLACK);
		setFont(70*scalingFactor);
		gc.fillText(String.valueOf(secsRemaining), (Game.backendWidth/2)*scalingFactor, (Game.backendHeight/2)*scalingFactor);

		gc.restore();
	}

	/**
	 * Draw a semi-transparent overlay over the game.
	 */
	public void drawOverlay() {
		gc.setFill(new Color(1, 1, 1, 0.5));
		gc.fillRect(0 , 0, width, height);
	}

	/**
	 * Draw a label for the game end screen.
	 * @param label the text to be displayed
	 */
	public void drawGameEndLabel(String label) {
		gc.setFill(Color.GRAY);
		setFont(30*scalingFactor);
		gc.setTextAlign(TextAlignment.LEFT);
		gc.fillText(label, (Game.backendWidth/2 + 70)*scalingFactor, height/2);
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
		return imageCache.computeIfAbsent(path + width + height, p -> new Image(getClass().getResource(path).toString(), width, height,
				false, true, false));
	}

	/**
	 * Gets the current Scene attached to the view
	 *
	 * @return the scene for the view
	 */
	public Scene getScene() {
		return scene;
	}

	/**
	 * Draw the high score board
	 * @param scores A list of the scores to display, with their index indicating the rank.
	 */
	public void drawScoreBoard(List<Score> scores) {

		// Constants for positioning
		final double Y_GAP = 40 * scalingFactor;
		final double RANK_START = 50 * scalingFactor;
		final double NAME_START = 150 * scalingFactor;
		final double NAME_MAX = 450 * scalingFactor;
		final double SCORE_START = 650 * scalingFactor;

		// Clear the screen
		clearCanvas();
		gc.setTextBaseline(VPos.CENTER);

		// Draw the heading
		gc.setFill(Color.GREEN);
		setFont(40*scalingFactor);
		gc.setTextAlign(TextAlignment.CENTER);
		gc.fillText("High Scores", width/2, Y_GAP);

		// Setup the font for the entries
		gc.setFill(Color.WHITE);
		setFont(30*scalingFactor);
		gc.setTextAlign(TextAlignment.LEFT);

		int rank = 1;
		double y = Y_GAP*2 + 30*scalingFactor;

		// Loop through each score
		for (Score score : scores) {

			// Draw the rank, name and score in a row
			gc.fillText(String.valueOf(rank), RANK_START, y);
			gc.fillText(score.getName(), NAME_START, y, NAME_MAX);
			gc.fillText(String.valueOf(score.getScoreValue()), SCORE_START, y);

			y += Y_GAP;
			++rank;
		}
	}

	/**
	 * Used to determine if a cursor should currently be showing based on the current time to give a blinking effect.
	 * @return True if a cursor should be shown.
	 */
	private boolean showCursor() {
		long time = System.currentTimeMillis();
		time = time/500;
		return time % 2 == 0;
	}

	/**
	 * Draw the screen for users to enter their name for the high score board.
	 * @param scoreValue The numeric value of he players score.
	 * @param name The name that has been entered so far.
	 */
	public void drawAddScore(int scoreValue, String name) {

		// Clear the screen
		clearCanvas();
		gc.setTextBaseline(VPos.CENTER);

		// Draw the heading
		gc.setFill(Color.GREEN);
		setFont(60*scalingFactor);
		gc.setTextAlign(TextAlignment.CENTER);
		gc.fillText("New High Score! (" + scoreValue + ")", width/2, 150*scalingFactor);

		// Setup the font for the name entry
		gc.setFill(Color.WHITE);
		setFont(30*scalingFactor);
		gc.setTextAlign(TextAlignment.LEFT);

		// Draw the name label and name
		name = showCursor() ? name + "|" : name;
		gc.fillText("Name:", 50*scalingFactor, 250*scalingFactor);
		gc.fillText(name, 150*scalingFactor, 250*scalingFactor, 650*scalingFactor);
	}

	/**
	 * Draw the story text full screen on top of a background image.
	 * @param backgroundImage The background image.
	 * @param message The text to display.
	 */
	public void drawStoryMessage(String backgroundImage, String message) {

		clearCanvas();
		gc.drawImage(getImage(backgroundImage, width, height), 0, 0);

		DropShadow dropShadow = new DropShadow();
		dropShadow.setOffsetY(3);
		dropShadow.setWidth(100);
		dropShadow.setColor(Color.BLACK);

		Text text = new Text(50*scalingFactor, 50*scalingFactor, message);
		Font font = Font.loadFont(fontUrl, 23*scalingFactor);
		text.setFont(font);
		text.setFill(Color.WHITE);
		text.setEffect(dropShadow);
		text.setWrappingWidth(width - 50*2*scalingFactor);

		pane.getChildren().add(text);
	}
}
