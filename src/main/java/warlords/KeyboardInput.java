package warlords;

import javafx.scene.input.KeyCode;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Class that listens to keyboard key presses for one user based on a provided key mapping.
 */
public class KeyboardInput implements IUserInput {

	private HashMap<KeyCode, InputType> keyMap = new HashMap<>();
	private InputType lastKeyPress;

	/**
	 * Create a new instance with a given key map for the player
	 * @param keyMap a hash map from the JavaFX {@link KeyCode} to their related {@link InputType}
	 */
	public KeyboardInput(HashMap<KeyCode, InputType> keyMap) {
		this.keyMap = keyMap;
	}

	// Checks if a given input type is a control key that should register only once or not
	private boolean isControlKey(InputType key) {

		List<InputType> controlKeys = Arrays.asList(
				InputType.MENU_DOWN,
				InputType.MENU_UP,
				InputType.MENU_SELECT,
				InputType.PAUSE,
				InputType.EXIT
		);
		return controlKeys.contains(key);
	}

	@Override
	public InputType getInputType() {
		InputType keyPress = lastKeyPress;
		if (isControlKey(keyPress)) {
			lastKeyPress = null;
		}
		return keyPress;
	}

	/**
	 * Register the press of the given key
	 * @param keyCode the key pressed
	 */
	public void keyPress(KeyCode keyCode) {
		InputType inputType = keyMap.get(keyCode);
		if (inputType != null) {
			lastKeyPress = inputType;
		}
	}

	/**
	 * Register the release of the given key
	 * @param keyCode the key released
	 */
	public void keyRelease(KeyCode keyCode) {
		InputType inputType = keyMap.get(keyCode);
		if (inputType == lastKeyPress) {
			lastKeyPress = null;
		}
	}
}
