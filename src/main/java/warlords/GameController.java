package warlords;

import warlordstest.IGame;
import java.util.ArrayList;
import static java.lang.Math.PI;

public class GameController implements IGame {

	private boolean loopRunning = false;
	private GameState state;
	private Game game;
	private Ball ball;
	private GameView gameView;
	private ArrayList<Paddle> paddles = new ArrayList<>(4);
	private ArrayList<IUserInput> players = new ArrayList<>(4);
	private ArrayList<Wall> walls = new ArrayList<>();
	private ArrayList<Warlord> warlords = new ArrayList<>(4);
	private ArrayList<IUserInput> userInputs;
	private boolean doExitGame = false;
	private boolean isPaused = false;
	private int timeRemaining = 120000;
	private long lastTimestamp;

	public GameController(ArrayList<IUserInput> userInputs, int width, int height, GameView gameView, GameState state) {
		this.userInputs = userInputs;
		game = new Game(width, height);
		this.gameView = gameView;
		this.state = state;
		setupStandardGameObjects();
	}

	public GameController(GameView gameView, ArrayList<Paddle> paddles, ArrayList<IUserInput> players, ArrayList<Wall> walls,
						  ArrayList<Warlord> warlords, Game game, Ball ball) {
		this.game = game;
		this.gameView = gameView;

		this.paddles = paddles;
		this.players = players;
		this.walls = walls;
		this.warlords = warlords;
		this.ball = ball;
		state = new GameState();
	}

	private void addWalls(int xOffset, int yOffset, double angleOffset, int owner) {

		double initialRadius = 150;
		double wallWidth = 40;
		double wallHeight = 15;
		double padding = 5;
		int rows = 3;

		double radius = initialRadius;
		for (int row=0; row<rows; row++) {
			double lengthAvailable = radius * PI/2;
			int num = (int) Math.floor((lengthAvailable) / (wallWidth + padding));

			double lengthUsed = num*wallWidth + (num+1)*padding;
			double unusedSpace = lengthAvailable - lengthUsed;
			double centerPadding = unusedSpace/2 ;
			double angle = angleOffset;

			for (int i = 0; i < num; i++) {
				if(i == 0) {
					angle += (centerPadding)/radius;
				}
					angle += (padding + wallWidth / 2) / radius;

				double x = radius * Math.cos(angle);
				double y = radius * Math.sin(angle);

				Wall wall = new Wall((int)x + xOffset, (int)y + yOffset, angle, owner);
				walls.add(wall);

				angle += (wallWidth / 2) / radius;
			}
			radius += wallHeight + padding;
		}
	}

	private void setupStandardGameObjects() {

		ball = new Ball(game.getWidth()/2, game.getHeight()/2);
		ball.generateRandomMovement(10);

		paddles.add(new Paddle(0, 0, 0.0, game));
		paddles.add(new Paddle(game.getWidth(), 0, PI/2, game));
		paddles.add(new Paddle(0, game.getHeight(), 3*PI/2, game));
		paddles.add(new Paddle(game.getWidth(), game.getHeight(), PI, game));

		int WARLORD_MARGIN = 50;
		warlords.add(new Warlord(WARLORD_MARGIN, WARLORD_MARGIN, "/knightBlue.png"));
		warlords.add(new Warlord(game.getWidth() - WARLORD_MARGIN, WARLORD_MARGIN, "/knightYellow.png"));
		warlords.add(new Warlord(WARLORD_MARGIN, game.getHeight() - WARLORD_MARGIN, "/knightRed.png"));
		warlords.add(new Warlord(game.getWidth() - WARLORD_MARGIN, game.getHeight() - WARLORD_MARGIN, "/knightGreen.png"));

		players.addAll(userInputs);
		for (int i=players.size(); i<4; i++) {
			players.add(new ArtificialUser(ball, paddles.get(i)));
		}

		addWalls(0, 0, 0, 0);
		addWalls(game.getWidth(), 0, PI/2, 1);
		addWalls(game.getWidth(), game.getHeight(), PI, 2);
		addWalls(0, game.getHeight(), 3*PI/2, 3);
	}

	public void beginGame() {
		loopRunning = true;
		lastTimestamp = System.nanoTime();
	}

	public void runLoop() {
		drawFrame();
		tick();
	}

	@Override
	public void tick() {

		if (!loopRunning) {
			return;
		}
		processInput();
		if (!isPaused) {
			checkCollisions();
			checkWinner();
			timeRemaining -= (System.nanoTime() - lastTimestamp) / 1e6;
			lastTimestamp = System.nanoTime();
		}
	}

	private void processInput() {

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
			} else if (input == InputType.PAUSE) {
				isPaused = false;
				lastTimestamp = System.nanoTime();
			}
			if (input == InputType.EXIT) {
				doExitGame = true;
			}
		}
	}

	private void checkCollisions() {
		CollisionDetector collisionDetector = new CollisionDetector(ball, paddles, walls, warlords, game);
		collisionDetector.moveBall();
	}

	private void drawFrame() {
		ArrayList<GameObject> gameObjects = new ArrayList<>(walls);
		gameObjects.addAll(warlords);
		gameObjects.addAll(paddles);
		gameObjects.add(ball);
		gameObjects.remove(null);
		gameView.drawObjects(gameObjects);
		gameView.drawTimer(timeRemaining/1000);
		if (isPaused) {
			gameView.drawPauseIndicator();
		}
	}

	@Override
	public boolean isFinished() {
		return state.getState() == GameState.State.FINISHED;
	}

	private void checkWinner() {
		//timeout - most walls
		int numPlayers = warlords.size();
		if (timeRemaining <= 0) {
			int[] ballOwners = new int[numPlayers];
			for (Wall selectedWall : walls) {
				ballOwners[selectedWall.getOwner()]++;
			}

			for (int i = 0; i < numPlayers; i++) {
				boolean hasWon = true;
				for (int j = 0; j < numPlayers; j++) {
					if (i == j) {
						continue;
					}
					if (ballOwners[i] <= ballOwners[j]) {
						hasWon = false;
					}
					if (hasWon) {
						warlords.get(i).setAsWinner();
						state.setState(GameState.State.FINISHED);
						return;
					}
				}
			}
		}

			for (int i = 0; i < numPlayers; i++) {
				boolean hasWon = true;
				for (int j = 0; j < numPlayers; j++) {
					if (i == j) {
						continue;
					}
					if (!warlords.get(j).isDead()) {
						hasWon = false;
					}
					if (hasWon) {
						warlords.get(i).setAsWinner();
						state.setState(GameState.State.FINISHED);
						return;
					}
				}
			}
		}

	@Override
	public void setTimeRemaining(int seconds) {
		timeRemaining = seconds*1000;
	}

	public boolean doExitGame() {
		return doExitGame;
	}
}
