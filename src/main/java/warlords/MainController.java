package warlords;

import javafx.scene.input.KeyCode;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Main controller class to create the {@link GameView} and coordinate giving control of
 * it to instances of {@link MenuController} and {@link GameController}.
 * Also sets up instances of {@link IUserInput} with default key mapping for {@link KeyboardInput} instances.
 */
public class MainController {

	// Set up 4 sets of key mappings with control keys attached to the first set.
	private HashMap<KeyCode, InputType> P1Map = new HashMap<>();
	private HashMap<KeyCode, InputType> P2Map = new HashMap<>();
	private HashMap<KeyCode, InputType> P3Map = new HashMap<>();
	private HashMap<KeyCode, InputType> P4Map = new HashMap<>();
	{
		P1Map.put(KeyCode.LEFT, InputType.LEFT);
		P1Map.put(KeyCode.RIGHT, InputType.RIGHT);
		P1Map.put(KeyCode.P, InputType.PAUSE);
		P1Map.put(KeyCode.ESCAPE, InputType.EXIT);
		P1Map.put(KeyCode.UP, InputType.MENU_UP);
		P1Map.put(KeyCode.DOWN, InputType.MENU_DOWN);
		P1Map.put(KeyCode.ENTER, InputType.MENU_SELECT);
		P1Map.put(KeyCode.PAGE_DOWN, InputType.END_TIME);
		P1Map.put(KeyCode.PAGE_UP, InputType.DELETE_WALLS);
		P2Map.put(KeyCode.A, InputType.LEFT);
		P2Map.put(KeyCode.D, InputType.RIGHT);
		P3Map.put(KeyCode.DIGIT4, InputType.LEFT);
		P3Map.put(KeyCode.DIGIT6, InputType.RIGHT);
		P4Map.put(KeyCode.NUMPAD4, InputType.LEFT);
		P4Map.put(KeyCode.NUMPAD6, InputType.RIGHT);
	}

	private MenuController menuController;
	private GameController gameController;
	private ArrayList<IUserInput> userInputs = new ArrayList<>();
	private GameView gameView;
	private SoundView soundView;
	private Game game;

	/**
	 * Create a new instance with the given game window dimensions
	 * @param height the height of the game window
	 * @param width the width of the game window
	 */
	public MainController(int height, int width) {
		game = new Game(width, height);
	}

	/**
	 * Instantiate all the objects and gives control to the menu.
	 * Sets up instances of {@link KeyboardInput} and the {@link GameView} with a {@link KeyListener}
	 * Also sets up the {@link SoundView}
	 * linking the scene from the GameView to the KeyboardInputs.
	 */
	public void start() {

		// Setup the 4 keyboard inputs using the 4 key mappings and add them to list of all inputs
		ArrayList<KeyboardInput> keyboardInputs = new ArrayList<>();
		keyboardInputs.add(new KeyboardInput(P1Map));
		keyboardInputs.add(new KeyboardInput(P2Map));
		keyboardInputs.add(new KeyboardInput(P3Map));
		keyboardInputs.add(new KeyboardInput(P4Map));
		userInputs.addAll(keyboardInputs);

		// Create the game and sound views
		gameView = new GameView(game.getWidth(), game.getHeight());
		soundView = new SoundView();

		// Create a key listener linking the scene to the keyboard inputs
		KeyListener listener = new KeyListener(gameView.getScene(), keyboardInputs);
		listener.startListening();

		// Create a new MenuController and link it to the game view and state
		menuController = new MenuController(userInputs, gameView, game);
		game.setState(Game.State.MENU);
	}

	/**
	 * Checks the current state (Menu or Game) and calls the processing loop of the appropriate controller.
	 * If the menu or game have finished switches control to the other controller and resets their state.
	 */
	public void runLoop() {
		// Check the state and run the loop of the appropriate controller
		switch (game.getState()) {
			case MENU:
				menuController.runLoop();
				// If the menu is ready for the game to be launched create a new game controller instance and start it
				if (menuController.doStartGame()) {
					game.setState(Game.State.GAME);
					gameController = new GameController(userInputs, game, gameView, soundView, Ages.NEOLITHIC);
					gameController.beginGame();
				}
				break;
			case GAME:
				gameController.runLoop();
				// If the game has finished discard the game controller and reset the menu ready for display
				if (gameController.doExitGame()) {
					game.setState(Game.State.MENU);
					gameController = null;
					menuController.reset();
				}
				break;
		}
	}
}