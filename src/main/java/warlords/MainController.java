package warlords;

import javafx.scene.input.KeyCode;
import java.util.ArrayList;
import java.util.HashMap;

public class MainController {

	private HashMap<KeyCode, InputType> P1Map = new HashMap<>();
	private HashMap<KeyCode, InputType> P2Map = new HashMap<>();
	private HashMap<KeyCode, InputType> P3Map = new HashMap<>();
	{
		P1Map.put(KeyCode.LEFT, InputType.LEFT);
		P1Map.put(KeyCode.RIGHT, InputType.RIGHT);
		P1Map.put(KeyCode.P, InputType.PAUSE);
		P2Map.put(KeyCode.A, InputType.LEFT);
		P2Map.put(KeyCode.D, InputType.RIGHT);
		P3Map.put(KeyCode.DIGIT4, InputType.LEFT);
		P3Map.put(KeyCode.DIGIT6, InputType.RIGHT);
	}

	private MenuController menuController = new MenuController();
	private GameController gameController;
	private ArrayList<IUserInput> userInputs = new ArrayList<>();
	private GameView gameView;

	public void start() {

		ArrayList<KeyboardInput> keyboardInputs = new ArrayList<>();
		keyboardInputs.add(new KeyboardInput(P1Map));
		keyboardInputs.add(new KeyboardInput(P2Map));
		keyboardInputs.add(new KeyboardInput(P3Map));
		userInputs.addAll(keyboardInputs);

		gameView = new GameView(800, 600);

		KeyListener listener = new KeyListener(gameView.getScene(), keyboardInputs);
		listener.startListening();

		gameController = new GameController(userInputs, 800, 600, gameView);
		gameController.beginGame();
	}

	public void runLoop() {

		if (gameController != null) {
			gameController.runLoop();
		}
	}
}
