package warlords;

import javafx.scene.input.KeyCode;
import warlordstest.IGame;
import java.util.ArrayList;
import java.util.HashMap;

public class MainController implements IGame {

	//TODO REMOVE THIS TEMP MAP
	static HashMap<KeyCode, InputType> P1Map = new HashMap<>();
	static {
		P1Map.put(KeyCode.LEFT, InputType.LEFT);
		P1Map.put(KeyCode.RIGHT, InputType.RIGHT);
	}

	private Ball ball;

	public Ball getBall() {
		return ball;
	}

	public void setupGameObjects() {
		ball = new Ball();
	}

	public void beginGame() {

		setupGameObjects();

		GameView gameView = new GameView();

		KeyboardInput player1 = new KeyboardInput(P1Map);
		KeyListener listener = new KeyListener(gameView.getScene(), new ArrayList<KeyboardInput>() {{
			add(player1);
		}});
		listener.startListening();
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
