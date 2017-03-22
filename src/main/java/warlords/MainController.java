package warlords;

import javafx.application.Application;
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

	public void beginGame() {
		//GridPane gridPane = new GridPane();
		//Scene scene = new Scene(gridPane);
		//GameView gameView = new GameView();
		//gameView.launch();
		Application.launch(GameView.class);
		ball = new Ball();
		KeyboardInput player1 = new KeyboardInput(P1Map);
		KeyListener listner = new KeyListener(GameView.getScene(), new ArrayList<KeyboardInput>() {{
			add(player1);
		}});
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
