package warlords;

import javafx.scene.input.KeyCode;
import java.util.HashMap;

public class KeyboardInput implements IUserInput {

	private HashMap<KeyCode, InputType> keyMap = new HashMap<>();
	private InputType lastKeyPress;

	public KeyboardInput(HashMap<KeyCode, InputType> keyMap) {
		this.keyMap = keyMap;
	}

	@Override
	public InputType getInputType() {
		InputType keyPress = lastKeyPress;
		if (keyPress == InputType.MENU_DOWN || keyPress == InputType.MENU_UP ||
				keyPress == InputType.MENU_SELECT || keyPress == InputType.PAUSE) {
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
