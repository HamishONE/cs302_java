package warlords;

import javafx.scene.input.KeyCode;
import warlordstest.IGame;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;

public class MainController implements IGame {

	//TODO REMOVE THIS TEMP MAP
	static HashMap<KeyCode, InputType> P1Map = new HashMap<>();
	static {
		P1Map.put(KeyCode.LEFT, InputType.LEFT);
		P1Map.put(KeyCode.RIGHT, InputType.RIGHT);
	}

	private Ball ball;
	private Paddle paddle;
	private GameView gameView;
	private KeyboardInput player1;
	private boolean isClosed = false;

	public Ball getBall() {
		return ball;
	}

	public Paddle getPaddle() {
		return paddle;
	}

	public void setupGameObjects() {
		ball = new Ball();
		paddle = new Paddle();
	}

	public void beginGame() {

		setupGameObjects();
		gameView = new GameView();

		player1 = new KeyboardInput(P1Map);
		KeyListener listener = new KeyListener(gameView.getScene(), new ArrayList<KeyboardInput>() {{
			add(player1);
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
						Thread.sleep(1);
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

		InputType inputType = player1.getInputType();
		if (inputType == InputType.LEFT) {
			paddle.moveLeft();
		} else if (inputType == InputType.RIGHT) {
			paddle.moveRight();
		}
	}

	private void drawFrame() {
		ArrayList<GameObject> gameObjects = new ArrayList<>();
		gameObjects.add(paddle);
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
