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
		P1Map.put(KeyCode.UP, InputType.MENU_UP);
		P1Map.put(KeyCode.DOWN, InputType.MENU_DOWN);
		P1Map.put(KeyCode.ENTER, InputType.MENU_SELECT);
		P2Map.put(KeyCode.A, InputType.LEFT);
		P2Map.put(KeyCode.D, InputType.RIGHT);
		P3Map.put(KeyCode.DIGIT4, InputType.LEFT);
		P3Map.put(KeyCode.DIGIT6, InputType.RIGHT);
	}

	private MenuController menuController;
	private GameController gameController;
	private ArrayList<IUserInput> userInputs = new ArrayList<>();
	private GameView gameView;
	private boolean gameStarted = false;

	public void start() {

		ArrayList<KeyboardInput> keyboardInputs = new ArrayList<>();
		keyboardInputs.add(new KeyboardInput(P1Map));
		keyboardInputs.add(new KeyboardInput(P2Map));
		keyboardInputs.add(new KeyboardInput(P3Map));
		userInputs.addAll(keyboardInputs);

		gameView = new GameView(800, 600);

		KeyListener listener = new KeyListener(gameView.getScene(), keyboardInputs);
		listener.startListening();

		menuController = new MenuController(userInputs, 800, 800, gameView);
	}

	public void runLoop() {

		if (!gameStarted && menuController != null) {
			menuController.runLoop();
			if (menuController.doStartGame()) {
				gameStarted = true;
				gameController = new GameController(userInputs, 800, 600, gameView);
				gameController.beginGame();
			}
		}
		if (gameController != null) {
			gameController.runLoop();
		}
	}
}
