package warlords;

import javafx.scene.input.KeyCode;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class KeyboardInput implements IUserInput {

	private HashMap<KeyCode, InputType> keyMap = new HashMap<>();
	private InputType lastKeyPress;

	public KeyboardInput(HashMap<KeyCode, InputType> keyMap) {
		this.keyMap = keyMap;
	}

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

	public void keyPress(KeyCode keyCode) {
		InputType inputType = keyMap.get(keyCode);
		if (inputType != null) {
			lastKeyPress = inputType;
		}
	}

	public void keyRelease(KeyCode keyCode) {
		InputType inputType = keyMap.get(keyCode);
		if (inputType == lastKeyPress) {
			lastKeyPress = null;
		}
	}
}
