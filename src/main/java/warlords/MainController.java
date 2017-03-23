package warlords;

import javafx.scene.input.KeyCode;
import warlordstest.IGame;
import java.util.ArrayList;
import java.util.HashMap;
import static java.lang.Math.PI;

public class MainController implements IGame {

	//TODO REMOVE THIS TEMP MAP
	static HashMap<KeyCode, InputType> P1Map = new HashMap<>();
	static HashMap<KeyCode, InputType> P2Map = new HashMap<>();
	static HashMap<KeyCode, InputType> P3Map = new HashMap<>();
	static {
		P1Map.put(KeyCode.LEFT, InputType.LEFT);
		P1Map.put(KeyCode.RIGHT, InputType.RIGHT);
		P2Map.put(KeyCode.A, InputType.LEFT);
		P2Map.put(KeyCode.D, InputType.RIGHT);
		P3Map.put(KeyCode.DIGIT4, InputType.LEFT);
		P3Map.put(KeyCode.DIGIT6, InputType.RIGHT);
	}

	private boolean loopRunning = false;

	private Game game;
	private Ball ball;
	private GameView gameView;
	private ArrayList<Paddle> paddles = new ArrayList<>(4);
	private ArrayList<IUserInput> players = new ArrayList<>(4);
	private ArrayList<Wall> walls = new ArrayList<>();

	// FOR TESTING
	public Ball getBall() {
		return ball;
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

		game = new Game(900, 600);

		ball = new Ball(game.getWidth()/2, game.getHeight()/2);
		ball.generateRandomMovement(5);

		paddles.add(new Paddle(0, 0, 0.0, game));
		paddles.add(new Paddle(game.getWidth(), 0, PI/2, game));
		paddles.add(new Paddle(0, game.getHeight(), 3*PI/2, game));
		paddles.add(new Paddle(game.getWidth(), game.getHeight(), PI, game));

		players.add(new KeyboardInput(P1Map));
		players.add(new KeyboardInput(P2Map));
		players.add(new KeyboardInput(P3Map));
		players.add(new ArtificialUser());

		addWalls(0, 0, 0);
		addWalls(game.getWidth(), 0, PI/2);
		addWalls(game.getWidth(), game.getHeight(), PI);
		addWalls(0, game.getHeight(), 3*PI/2);
	}

	public void beginGame() {

		setupGameObjects();
		gameView = new GameView(game);

		KeyListener listener = new KeyListener(gameView.getScene(), new ArrayList<KeyboardInput>() {{
			add((KeyboardInput) players.get(0));
			add((KeyboardInput) players.get(1));
			add((KeyboardInput) players.get(2));
		}});
		listener.startListening();

		loopRunning = true;
	}

	public void runLoop() {

		if (!loopRunning) {
			return;
		}

		processInput();
		tick();
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

		if (ball.getXPos() > game.getWidth()) {
			ball.rebound(Math.PI/2);
		}
		else if (ball.getXPos() < 0) {
			ball.rebound(Math.PI/2);
		}
		else if (ball.getYPos() > game.getHeight()) {
			ball.rebound(0);
		}
		else if (ball.getYPos() < 0) {
			ball.rebound(0);
		}

		for (Paddle paddle : paddles) {
			int xDiff = Math.abs(ball.getXPos() - paddle.getXPos());
			int yDiff = Math.abs(ball.getYPos() - paddle.getYPos());
			if (xDiff < 20 && yDiff < 20) {
				double surfaceAngle = paddle.getAngle() - PI/2;
				ball.rebound(surfaceAngle);
			}
		}
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
