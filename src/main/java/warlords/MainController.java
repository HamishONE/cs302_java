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

	private boolean isClosed = false;

	private Game game;
	private Ball ball;
	private GameView gameView;
	private ArrayList<Paddle> paddles = new ArrayList<>(4);
	private ArrayList<IUserInput> players = new ArrayList<>(4);

	// FOR TESTING
	public Ball getBall() {
		return ball;
	}

	public void setupGameObjects() {

		game = new Game(900, 600);
		ball = new Ball(0, 0);

		paddles.add(new Paddle(0, 0, 0.0, game));
		paddles.add(new Paddle(900, 0, PI/2, game));
		paddles.add(new Paddle(0, 600, 3*PI/2, game));
		paddles.add(new Paddle(900, 600, PI, game));


		players.add(new KeyboardInput(P1Map));
		players.add(new KeyboardInput(P2Map));
		players.add(new KeyboardInput(P3Map));
		players.add(new ArtificialUser());
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

		Thread thread = new Thread() {
			@Override
			public void run() {
				while (!isClosed) {
					processInput();
					tick();
					drawFrame();
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						System.out.println("XCEPpttetl");
						e.printStackTrace();
					}
				}
			}
		};
		thread.setName("Work Harder");
		thread.start();
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

	private void drawFrame() {
		ArrayList<GameObject> gameObjects = new ArrayList<>();
		gameObjects.addAll(paddles);
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

	void close() {
		isClosed = true;
	}
}
