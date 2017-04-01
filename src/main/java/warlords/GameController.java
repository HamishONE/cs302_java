package warlords;

import warlordstest.IGame;
import java.util.ArrayList;
import java.util.Objects;
import static java.lang.Math.PI;

/**
 * Controller class for the game instance, creates all of the {@link GameObject} and passes them to the
 * {@link GameView} to be rendered when required.
 * <br>
 * Also co-ordinates all of the collision and winner checking for the main game loop
 */
public class GameController implements IGame {

	private boolean loopRunning = false;
	private Game game;
	private Ball ball;
	private GameView gameView;
	private SoundView soundView;
	private ArrayList<Paddle> paddles = new ArrayList<>(4);
	private ArrayList<IUserInput> players = new ArrayList<>(4);
	private ArrayList<Wall> walls = new ArrayList<>();
	private ArrayList<Warlord> warlords = new ArrayList<>(4);
	private ArrayList<IUserInput> userInputs;
	private boolean doExitGame = false;
	private boolean isPaused = false;
	private int timeRemaining = 120000;
	private long lastTimestamp;

	/**
	 * Create a new instance of a controller
	 *
	 * @param userInputs	An arrayList of "players" to control the paddles, order is important
	 * @param game 			Instance of the game model which stores information about the game
	 * @param gameView		Instance of main view of the game
	 * @param soundView		Instance of sound player view for the game to use
	 */
	public GameController(ArrayList<IUserInput> userInputs, Game game, GameView gameView, SoundView soundView) {
		//Initialize all parameters passed in through the constructor
		this.userInputs = userInputs;
		this.game = game;
		this.gameView = gameView;
		this.soundView = soundView;

		//Set up a standard game
		setupStandardGameObjects();
	}

	/**
	 * Create a new instance of a controller, specifically for testing
	 *
	 * @param gameView	Instance of main view of the game
	 * @param paddles	ArrayList of the paddles to be in the game
	 * @param players	ArrayList of the UserInputs - "players" to be in the game
	 * @param walls		ArrayList of the wall instances to be in the game
	 * @param warlords	ArrayList of the warlords to be in the game
	 * @param game		Instance of game model for the controller
	 * @param ball		Instance of the ball to be in the game
	 */
	public GameController(GameView gameView, ArrayList<Paddle> paddles, ArrayList<IUserInput> players, ArrayList<Wall> walls,
						  ArrayList<Warlord> warlords, Game game, Ball ball) {
		//Set local variables specifically to only include the items included in the constructors
		this.game = game;
		this.gameView = gameView;
		this.paddles = paddles;
		this.players = players;
		this.walls = walls;
		this.warlords = warlords;
		this.ball = ball;
	}

	/**
	 * Method to add walls to the game.
	 * Given a set of inputs, this will generate as many walls as possible around a radius from a specified location
	 *
	 * @param xOffset		X offset from origin for walls to be moved
	 * @param yOffset		Y offset from origin for walls to be moved
	 * @param angleOffset	Offset to determine which quarter of the circle should be populated
	 * @param owner			Int to represent which player the walls "belong" to
	 */
	private void addWalls(int xOffset, int yOffset, double angleOffset, int owner) {

		//Initial hard coding of number of rows, space between walls, and radius to render them on
		double initialRadius = 150;
		double wallWidth = 40;
		double wallHeight = 15;
		double padding = 5;
		int rows = 3;

		// Loop through the number of rows to be createdS
		double radius = initialRadius;
		for (int row=0; row<rows; row++) {

			//Calculate the length available along the selected radius and calculate how many walls fit on it
			double lengthAvailable = radius * PI/2;
			int num = (int) Math.floor((lengthAvailable) / (wallWidth + padding));

			//Find used portion of this circumference, unused portion, and side padding in order to center walls
			double lengthUsed = num*wallWidth + (num+1)*padding;
			double unusedSpace = lengthAvailable - lengthUsed;
			double centerPadding = unusedSpace/2 ;
			double angle = angleOffset;

			//Loop through the number of walls along the current radius and place them accordingly, based on incrementing angle.
			for (int i = 0; i < num; i++) {
				if(i == 0) {
					angle += (centerPadding)/radius;
				}
					angle += (padding + wallWidth / 2) / radius;

				double x = radius * Math.cos(angle);
				double y = radius * Math.sin(angle);

				Wall wall = new Wall((int)x + xOffset, (int)y + yOffset, angle+PI, owner);
				walls.add(wall);

				angle += (wallWidth / 2) / radius;
			}
			//Increment radius by height of wall and padding to prepare for next layer of walls
			radius += wallHeight + padding;
		}
	}

	/**
	 * Helper method which goes through and adds all the standard game objects this includes:
	 *
	 * <ul>
	 *     <li>A ball - initial movement is randomly generated</li>
	 *     <li>A paddle for each of the 4 players</li>
	 *     <li>A warlord instance for each of the 4 players</li>
	 *     <li>Assigns all the userInputs(Players and AI) to the paddles</li>
	 *     <li>Creates a wall set for each of the corners</li>
	 * </ul>
	 */
	private void setupStandardGameObjects() {
		//Add ball in center and set it on its way
		ball = new Ball(game.getWidth()/2, game.getHeight()/2);
		ball.generateRandomMovement(10);

		//Add paddles in each corner
		paddles.add(new Paddle(0, 0, 0.0, game));
		paddles.add(new Paddle(game.getWidth(), 0, PI/2, game));
		paddles.add(new Paddle(0, game.getHeight(), 3*PI/2, game));
		paddles.add(new Paddle(game.getWidth(), game.getHeight(), PI, game));

		//Add warlords in each corner
		int WARLORD_MARGIN = 50;
		warlords.add(new Warlord(WARLORD_MARGIN, WARLORD_MARGIN, "/knightBlue.png"));
		warlords.add(new Warlord(game.getWidth() - WARLORD_MARGIN, WARLORD_MARGIN, "/knightYellow.png"));
		warlords.add(new Warlord(WARLORD_MARGIN, game.getHeight() - WARLORD_MARGIN, "/knightRed.png"));
		warlords.add(new Warlord(game.getWidth() - WARLORD_MARGIN, game.getHeight() - WARLORD_MARGIN, "/knightGreen.png"));

		// Add the user inputs for the number of human players
		if (game.getNumHumanPlayers() > userInputs.size()) {
			throw new RuntimeException("Only " + userInputs.size() + " player inputs are set up, but a " +
					game.getNumHumanPlayers() + " player mode has been selected.");
		}
		for (int i = 0; i<game.getNumHumanPlayers(); i++) {
			players.add(userInputs.get(i));
		}

		// Add AI players suc that all 4 paddles are controlled
		for (int i=game.getNumHumanPlayers(); i<4; i++) {
			players.add(new ArtificialUser(ball, paddles.get(i)));
		}

		//Add walls to each corner
		addWalls(0, 0, 0, 0);
		addWalls(game.getWidth(), 0, PI/2, 1);
		addWalls(game.getWidth(), game.getHeight(), PI, 2);
		addWalls(0, game.getHeight(), 3*PI/2, 3);
	}

	/**
	 * Starts the runtime loop to begin the game
	 * Initializes the clock time to begin the countdown timer
	 */
	public void beginGame() {
		loopRunning = true;
		lastTimestamp = System.nanoTime();
	}

	/**
	 * Has to be done so that tick can be used in testing, isolating it from the graphics engine
	 */
	public void runLoop() {
		drawFrame();
		tick();
	}

	@Override
	public void tick() {

		if (!loopRunning) {
			return;
		}
		//Check for inputs, if running, do loop functions to find collisions and see if game has been won
		processInput();
		if (!isPaused) {
			checkCollisions();
			checkWinner();
			timeRemaining -= (System.nanoTime() - lastTimestamp) / 1e6;
			lastTimestamp = System.nanoTime();
		}
	}

	/**
	 * Processes the inputs returned from each of the players (keyboard and AI)
	 */
	private void processInput() {
		//Loop through each player, checking what they have "pressed", handle accordingly
		for (int i=0; i<players.size(); i++) {
			InputType input = players.get(i).getInputType();
			if (input != null && !isPaused) {
				switch (input) {
					case LEFT:
						if (!warlords.get(i).isDead()) {
							paddles.get(i).moveLeft();
						}
						break;
					case RIGHT:
						if (!warlords.get(i).isDead()) {
							paddles.get(i).moveRight();
						}
						break;
					case PAUSE:
						isPaused = true;
						break;
				}
			}
			//Check if pause or exit keys have been pressed, and action regardless of player
			else if (input == InputType.PAUSE) {
				isPaused = false;
				lastTimestamp = System.nanoTime();
			}
			if (input == InputType.EXIT) {
				doExitGame = true;
			}
		}
	}

	/**
	 * Checks for collisions, just uses a CollisionDetector
	 *
	 * @see CollisionDetector
	 */
	private void checkCollisions() {
		CollisionDetector collisionDetector = new CollisionDetector(ball, paddles, walls, warlords, game, soundView);
		collisionDetector.moveBall();
	}

	/**
	 * Draws a frame by creating a list of all the objects on the screen and passes them to the view to be rendered
	 *
	 * Also passes the time remaining and the pause text when applicable.
	 */
	private void drawFrame() {
		//Add all objects to an ArrayList
		ArrayList<GameObject> gameObjects = new ArrayList<>(walls);
		gameObjects.addAll(warlords);
		gameObjects.addAll(paddles);
		gameObjects.add(ball);
		gameObjects.removeIf(Objects::isNull);

		//pass all objects from the ArrayList to the gameView to be rendered
		gameView.drawObjects(gameObjects);
		gameView.drawTimer(timeRemaining/1000);
		if (isPaused) {
			gameView.drawPauseIndicator();
		}
	}

	@Override
	public boolean isFinished() {
		return game.getState() == Game.State.FINISHED;
	}

	/**
	 * Check if someone has won
	 *
	 * If game time has ended, loops through and determines the player with the most walls remaining
	 *
	 * If there is only one warlord left standing, they are the winner.
	 */
	private void checkWinner() {

		int numPlayers = warlords.size();
		//When time has run out, loop through all walls and count number remaining in an array
		if (timeRemaining <= 0) {
			int[] ballOwners = new int[numPlayers];
			for (Wall selectedWall : walls) {
				ballOwners[selectedWall.getOwner()]++;
			}

			//Nested loop to find which player has the most walls
			for (int i = 0; i < numPlayers; i++) {
				boolean hasWon = true;
				for (int j = 0; j < numPlayers; j++) {
					if (i == j) {
						continue;
					}
					if (ballOwners[i] <= ballOwners[j]) {
						hasWon = false;
						break;
					}
				}
				//If someone has won, end the game and set them as the winner
				if (hasWon) {
					warlords.get(i).setAsWinner();
					game.setState(Game.State.FINISHED);
					return;
				}
			}
		}

		//Nested loop to check if there is only one player standing
		for (int i = 0; i < numPlayers; i++) {
			boolean hasWon = true;
			for (int j = 0; j < numPlayers; j++) {
				if (i != j && !warlords.get(j).isDead()) {
					hasWon = false;
					break;
				}
			}
			//If someone has won, end the game and set them as the winner
			if (hasWon) {
				warlords.get(i).setAsWinner();
				game.setState(Game.State.FINISHED);
				return;
			}
		}
	}

	@Override
	public void setTimeRemaining(int seconds) {
		timeRemaining = seconds*1000;
	}

	/**
	 * Used when esc is pushed to return to main menu - to be refactored
	 * @return boolean if game should close
	 */
	public boolean doExitGame() {
		return doExitGame;
	}
}
