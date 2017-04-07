package warlords;

import warlordstest.IGame;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import static java.lang.Math.PI;
import static java.lang.Math.random;

/**
 * Controller class for the game instance, creates all of the {@link GameObject} and passes them to the
 * {@link GameView} to be rendered when required.
 * <br>
 * Also co-ordinates all of the collision and winner checking for the main game loop
 */
public class GameController implements IGame {

	private enum InternalState {
		IDLE, PAUSED, RUNNING, ENDED, EXITING, CONFIRM_EXIT
	}

	// Constants
	private final static int GAME_TIME = 120000;
	private final static int COUNTDOWN_TIME = 3000;
	private final static double BALL_SPEED = 10; //TODO: Replace with a range of speeds

	// Instance variables
	private Game game;
	private GameView gameView;
	private SoundView soundView;
	private ArrayList<IUserInput> players = new ArrayList<>();
	private ArrayList<IUserInput> userInputs;
	private InternalState internalState = InternalState.IDLE;
	private int timeRemaining = GAME_TIME + COUNTDOWN_TIME;
	private long lastTimestamp;
	private boolean difficultyIncrease1, difficultyIncrease2, difficultyIncrease3, difficultyIncrease4 = false;
	private Ages age;

	// Game objects
	private ArrayList<Wall> walls = new ArrayList<>();
	private ArrayList<Warlord> warlords = new ArrayList<>();
	private ArrayList<Ball> balls = new ArrayList<>();
	private ArrayList<Paddle> paddles = new ArrayList<>();
	private Boundary boundary;

	/**
	 * Create a new instance of a controller
	 *
	 * @param userInputs	An arrayList of "players" to control the paddles, order is important
	 * @param game 			Instance of the game model which stores information about the game
	 * @param gameView		Instance of main view of the game
	 * @param soundView		Instance of sound player view for the game to use
	 */
	public GameController(ArrayList<IUserInput> userInputs, Game game, GameView gameView, SoundView soundView, Ages age) {
		//Initialize all parameters passed in through the constructor
		this.userInputs = userInputs;
		this.game = game;
		this.gameView = gameView;
		this.soundView = soundView;
		this.age = age;

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
						  ArrayList<Warlord> warlords, Game game, Ball ball, Boundary boundary) {
		//Set local variables specifically to only include the items included in the constructors
		this.game = game;
		this.gameView = gameView;
		this.paddles = paddles;
		this.players = players;
		this.walls = walls;
		this.warlords = warlords;
		this.balls.add(ball);
		this.boundary = boundary;
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
	private void addWalls(int xOffset, int yOffset, double angleOffset, int owner, Ages age) {

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

				//Generate a random powerup for the walls
				PowerUp powerUp;
				int seed = (int)(random() *20.0) ;
				switch (seed) {
					case 0:
						powerUp = PowerUp.BALL_FASTER;
						break;
					case 1:
						powerUp = PowerUp.BALL_SLOWER;
						break;
					case 2:
						powerUp = PowerUp.PADDLE_FASTER;
						break;
					case 3:
						powerUp = PowerUp.PADLLE_SLOWER;
						break;
					case 4:
						powerUp = PowerUp.PADDLE_GROW;
						break;
					case 5:
						powerUp = PowerUp.PADDLE_SHRINK;
						break;
					default:
						powerUp = null;
						break;
				}

				Wall wall = new Wall((int)x + xOffset, (int)y + yOffset, angle+PI, owner, age, powerUp);
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
		//Add boundary, and therefore background
		boundary = new Boundary(age);

		//Add ball in center and set it on its way
		addBall(BALL_SPEED);

		//Add paddles in each corner
		paddles.add(new Paddle(0, 0, age, 0.0, game, balls));
		paddles.add(new Paddle(Game.backendWidth, 0, age, PI/2, game, balls));
		paddles.add(new Paddle(0, Game.backendHeight, age, 3*PI/2, game, balls));
		paddles.add(new Paddle(Game.backendWidth, Game.backendHeight, age, PI, game, balls));

		//Add warlords in each corner
		int WARLORD_MARGIN = 50;
		warlords.add(new Warlord(WARLORD_MARGIN, WARLORD_MARGIN, age));
		warlords.add(new Warlord(Game.backendWidth - WARLORD_MARGIN, WARLORD_MARGIN, age));
		warlords.add(new Warlord(WARLORD_MARGIN, Game.backendHeight - WARLORD_MARGIN, age));
		warlords.add(new Warlord(Game.backendWidth - WARLORD_MARGIN, Game.backendHeight - WARLORD_MARGIN, age));

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
			players.add(new ArtificialUser(balls.get(0), paddles.get(i))); //TODO: The AI will use all balls
		}

		//Add walls to each corner
		addWalls(0, 0, 0, 0, age);
		addWalls(Game.backendWidth, 0, PI/2, 1, age);
		addWalls(Game.backendWidth, Game.backendHeight, PI, 2, age);
		addWalls(0, Game.backendHeight, 3*PI/2, 3, age);

		//Preload all the game sounds
		ArrayList<String> soundPaths = new ArrayList<String>() {{
			add(boundary.getSoundPath());
			add(balls.get(0).getSoundPath());
			add(warlords.get(0).getSoundPath());
		}};
		soundView.loadSounds(soundPaths);
	}

	/**
	 * Add a ball to the centre of the game with the given speed and a random direction.
	 * @param speed The speed of the new ball.
	 */
	private void addBall(double speed) {
		Ball ball = new Ball(Game.backendWidth/2, Game.backendHeight/2, age);
		ball.generateRandomMovement(speed);
		balls.add(ball);
	}

	/**
	 * Starts the runtime loop to begin the game
	 * Initializes the clock time to begin the countdown timer
	 */
	public void beginGame() {
		internalState = InternalState.RUNNING;
		lastTimestamp = System.nanoTime();
	}

	/**
	 * Updates the time remaining based on the system clock.
	 */
	private void updateTimer() {
		if (internalState == InternalState.RUNNING) {
			timeRemaining -= (System.nanoTime() - lastTimestamp) / 1e6;
			lastTimestamp = System.nanoTime();
		}
	}

	/**
	 * Has to be done so that tick can be used in testing, isolating it from the graphics engine
	 */
	public void runLoop() {
		processControlInput();
		if (internalState == InternalState.RUNNING || internalState == InternalState.PAUSED || internalState == InternalState.CONFIRM_EXIT) {
			updateTimer();
			if (timeRemaining > GAME_TIME) {
				drawFrame(false);
				gameView.drawCountdown((timeRemaining - GAME_TIME)/1000 + 1);
			} else {
				drawFrame(true);
				tick();
			}
		}
	}

	@Override
	public void tick() {
		//Check for inputs, if running, do loop functions to find collisions and see if game has been won
		if (internalState != InternalState.PAUSED && internalState != InternalState.CONFIRM_EXIT) {
			processGameInput();
			checkCollisions();
			checkWinner();

			//Make game harder at a couple of points in the game
			//Add extra ball somewhere between 5 and 15 seconds
			if (timeRemaining < (GAME_TIME - (5000+random()*10000)) && !difficultyIncrease1) {
				addBall(BALL_SPEED);
				difficultyIncrease1 = true;
			}

			//Speed all balls up somewhere from 20 to 30 seconds left
			if(timeRemaining < (20000+random()*10000) && !difficultyIncrease2) {
				for(Ball ball : balls) {
					ball.multiplySpeed(1.2);
				}
				difficultyIncrease2 = true;
			}

			//Shrink all paddles when there is somewhere from 10 to 20 seconds left
			if(timeRemaining < (10000+random()*10000) && !difficultyIncrease3) {
				for(Paddle paddle : paddles) {
					if(paddle != null) {
						paddle.modifyWidth(-30);
					}
				}
				difficultyIncrease3 = true;
			}

			//Add another ball when there are 5 to 10 seconds left
			if(timeRemaining < (5000+random()*5000) && !difficultyIncrease4) {
				addBall(BALL_SPEED+5);
				difficultyIncrease4 = true;
			}


		}
	}

	/**
	 * Processes the inputs returned from each of the players relating to game play (keyboard and AI)
	 */
	private void processGameInput() {
		//Loop through each player, checking what they have "pressed", handle accordingly
		for (int i=0; i<players.size(); i++) {
			InputType input = players.get(i).getInputType();
			if (input != null && !warlords.get(i).isDead()) {
				if (input == InputType.LEFT) {
					paddles.get(i).moveLeft();
				} else if (input == InputType.RIGHT) {
					paddles.get(i).moveRight();
				}
			}
		}
	}

	/**
	 * Processes the inputs returned from each of the users attached relating to system control(keyboard)
	 */
	private void processControlInput() {
		//Loop through each user, checking what they have "pressed", handle accordingly
		for (IUserInput userInput : userInputs) {
			InputType input = userInput.getInputType();
			if (input != null) {
				switch (input) {
					case PAUSE:
						if (internalState == InternalState.PAUSED) {
							internalState = InternalState.RUNNING;
							lastTimestamp = System.nanoTime();
						} else if (internalState == InternalState.RUNNING) {
							internalState = InternalState.PAUSED;
						}
						break;
					case DELETE_WALLS:
						walls.clear();
						break;
					case END_TIME:
						timeRemaining = 3000;
						break;
					case EXIT:
						if(internalState == InternalState.CONFIRM_EXIT) {
							internalState = InternalState.EXITING;
						}
						else {
							internalState = InternalState.CONFIRM_EXIT;
						}
						break;
					case MENU_SELECT:
						if (internalState == InternalState.ENDED) {
							internalState = InternalState.EXITING;
						}
						else if(internalState == InternalState.CONFIRM_EXIT) {
							internalState = InternalState.RUNNING;
							lastTimestamp = System.nanoTime();
						}
						else if (timeRemaining > GAME_TIME) {
							timeRemaining = GAME_TIME;
						}
						break;
				}
			}
		}
	}

	/**
	 * Checks for collisions between each ball and other objects, uses a CollisionDetector for each ball.
	 *
	 * @see CollisionDetector
	 */
	private void checkCollisions() {
		for (Ball ball : balls) {
			new CollisionDetector(ball, paddles, walls, warlords, boundary, game, soundView).moveBall();
		}
	}

	/**
	 * Draws a frame by creating a list of all the objects on the screen and passes them to the view to be rendered
	 * Also passes the time remaining and the pause text when applicable.
	 * @param showBalls Whether to show the balls or not.
	 */
	private void drawFrame(boolean showBalls) {

		//Add all game objects to a list
		ArrayList<GameObject> gameObjects = new ArrayList<>(walls);
		gameObjects.addAll(warlords);
		gameObjects.addAll(paddles);
		if (showBalls) {
			gameObjects.addAll(balls);
		}
		gameObjects.removeIf(Objects::isNull);

		// Pass the list of objects to the game view to be rendered
		gameView.clearCanvas();
		gameView.drawObjects(gameObjects);

		// If the game has started show how long is remaining, otherwise show the game time limit
		gameView.drawTimer(timeRemaining <= GAME_TIME ? timeRemaining/1000 + 1 : GAME_TIME/1000);

		// If the game is paused show the paused indicator
		if (internalState == InternalState.PAUSED) {
			gameView.drawPauseIndicator();
		}
		else if (internalState == InternalState.CONFIRM_EXIT) {
			gameView.drawExitConfirm();
		}
	}

	@Override
	public boolean isFinished() {
		return game.getState() == Game.State.FINISHED;
	}

	/**
	 * Moves the winning warlord to centre and displays a label with their name.
	 * @param winner the player that has won, or null for a draw
	 */
	private void processGameEnd(Warlord winner) {
		internalState = InternalState.ENDED;
		//Check if not null for testing purposes
		if (gameView == null) {
			if (winner != null) {
				winner.setAsWinner();
			}
		} else {
			gameView.drawOverlay();
			if (winner != null) {
				winner.setAsWinner();
				gameView.drawWinnerLabel("Player " + (warlords.indexOf(winner) + 1));
				winner.setXPos(Game.backendWidth / 2);
				winner.setYPos(Game.backendHeight / 2);
				winner.setDimensions(120, 180);
				gameView.drawObjects(Collections.singletonList(winner));
			} else {
				gameView.drawWinnerLabel(null);
			}
		}
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
					processGameEnd(warlords.get(i));
					return;
				}
			}
			processGameEnd(null);
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
				processGameEnd(warlords.get(i));
				return;
			}
		}
	}

	@Override
	public void setTimeRemaining(int seconds) {
		timeRemaining = seconds*1000;
	}

	/**
	 * True when the game is finish and we should return to main menu
	 * @return boolean if game should close
	 */
	public boolean doExitGame() {
		return internalState == InternalState.EXITING;
	}
}
