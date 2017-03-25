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
	private ArrayList<IUserInput> userInputs;

	public GameController(ArrayList<IUserInput> userInputs, int width, int height, GameView gameView) {
		this.userInputs = userInputs;
		game = new Game(width, height);
		this.gameView = gameView;
	}

	private void addWalls(int xOffset, int yOffset, double angleOffset) {

		double initialRadius = 150;
		double wallWidth = 20;
		double padding = 2;
		int rows = 3;

		double radius = initialRadius;
		for (int row=0; row<rows; row++) {
			double lengthAvailable = radius * PI/2;
			int num = (int) Math.floor((lengthAvailable - padding) / (wallWidth + padding));

			double lengthUsed = num*wallWidth + (num+1)*padding;
			double unusedSpace = lengthAvailable - lengthUsed;
			double additionalPadding = unusedSpace / (num+1);
			padding += additionalPadding;
			double angle = angleOffset;

			for (int i = 0; i < num; i++) {

				angle += (padding + wallWidth / 2) / radius;

				double x = radius * Math.cos(angle);
				double y = radius * Math.sin(angle);

				Wall wall = new Wall((int)x + xOffset, (int)y + yOffset);
				walls.add(wall);

				angle += (wallWidth / 2) / radius;
			}
			radius += wallWidth + padding*2;
		}
	}

	public void setupGameObjects() {

		ball = new Ball(game.getWidth()/2, game.getHeight()/2);
		ball.generateRandomMovement(1);

		paddles.add(new Paddle(0, 0, 0.0, game));
		paddles.add(new Paddle(game.getWidth(), 0, PI/2, game));
		paddles.add(new Paddle(0, game.getHeight(), 3*PI/2, game));
		paddles.add(new Paddle(game.getWidth(), game.getHeight(), PI, game));

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

		if (!loopRunning) {
			return;
		}

		processInput();
		checkCollisions();
		drawFrame();
	}

	private void processInput() {

		for (int i=0; i<4; i++) {
			InputType input = players.get(i).getInputType();
			if (input == InputType.LEFT) {
				paddles.get(i).moveLeft();
			} else if (input == InputType.RIGHT) {
				paddles.get(i).moveRight();
			}
		}
	}

	private void checkCollisions() {
		CollisionDetector collisionDetector = new CollisionDetector(ball, paddles, game);
		collisionDetector.moveBall(5);
	}

	private void drawFrame() {
		ArrayList<GameObject> gameObjects = new ArrayList<>();
		gameObjects.add(ball);
		gameObjects.addAll(paddles);
		gameObjects.addAll(walls);
		gameView.drawObjects(gameObjects);
	}

	@Override
	public void tick() {
		ball.tick();
	}

	@Override
	public boolean isFinished() {
		return false;
	}

	@Override
	public void setTimeRemaining(int seconds) {

	}
}
