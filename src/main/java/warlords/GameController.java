package warlords;

import warlordstest.IGame;
import java.util.ArrayList;
import static java.lang.Math.PI;

public class GameController implements IGame {

	private boolean loopRunning = false;
	private Game game;
	public Ball ball;
	private GameView gameView;
	private ArrayList<Paddle> paddles = new ArrayList<>(4);
	private ArrayList<IUserInput> players = new ArrayList<>(4);
	private ArrayList<Wall> walls = new ArrayList<>();
	private ArrayList<Warlord> warlords = new ArrayList<>(4);
	private ArrayList<IUserInput> userInputs;
	private boolean doExitGame = false;
	private boolean isPaused = false;

	public GameController(ArrayList<IUserInput> userInputs, int width, int height, GameView gameView) {
		this.userInputs = userInputs;
		game = new Game(width, height);
		this.gameView = gameView;
	}

	private void addWalls(int xOffset, int yOffset, double angleOffset) {

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

				Wall wall = new Wall((int)x + xOffset, (int)y + yOffset, angle);
				walls.add(wall);

				angle += (wallWidth / 2) / radius;
			}
			radius += wallHeight + padding;
		}
	}

	public void setupGameObjects() {

		ball = new Ball(game.getWidth()/2, game.getHeight()/2);
		ball.generateRandomMovement(10);

		paddles.add(new Paddle(0, 0, 0.0, game));
		paddles.add(new Paddle(game.getWidth(), 0, PI/2, game));
		paddles.add(new Paddle(0, game.getHeight(), 3*PI/2, game));
		paddles.add(new Paddle(game.getWidth(), game.getHeight(), PI, game));

		int WARLORD_MARGIN = 50;
		warlords.add(new Warlord(WARLORD_MARGIN, WARLORD_MARGIN));
		warlords.add(new Warlord(game.getWidth() - WARLORD_MARGIN, WARLORD_MARGIN));
		warlords.add(new Warlord(WARLORD_MARGIN, game.getHeight() - WARLORD_MARGIN));
		warlords.add(new Warlord(game.getWidth() - WARLORD_MARGIN, game.getHeight() - WARLORD_MARGIN));

		players.addAll(userInputs);
		for (int i=players.size(); i<4; i++) {
			players.add(new ArtificialUser(ball, paddles.get(i)));
		}

		addWalls(0, 0, 0);
		addWalls(game.getWidth(), 0, PI/2);
		addWalls(game.getWidth(), game.getHeight(), PI);
		addWalls(0, game.getHeight(), 3*PI/2);
	}

	public void beginGame() {

		setupGameObjects();
		loopRunning = true;
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
		if (isPaused) {
			gameView.drawPauseIndicator();
		}
		processInput();
		if (!isPaused) {
			checkCollisions();
		}
	}

	private void processInput() {

		for (int i=0; i<4; i++) {
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
			}
			if (input == InputType.EXIT) {
				doExitGame = true;
			}
		}
	}

	private void checkCollisions() {
		CollisionDetector collisionDetector = new CollisionDetector(ball, paddles, walls, warlords, game);
		collisionDetector.moveBall(5);
	}

	private void drawFrame() {
		ArrayList<GameObject> gameObjects = new ArrayList<>(walls);
		gameObjects.addAll(warlords);
		gameObjects.addAll(paddles);
		gameObjects.add(ball);
		gameView.drawObjects(gameObjects);
	}

	@Override
	public boolean isFinished() {
		return false;
	}

	@Override
	public void setTimeRemaining(int seconds) {

	}

	public boolean doExitGame() {
		return doExitGame;
	}
}
