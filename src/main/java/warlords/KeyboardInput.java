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
		return lastKeyPress;
	}

	public void keyPress(KeyCode keyCode) {
		InputType inputType = keyMap.get(keyCode);
		if(inputType != null) {
			lastKeyPress = inputType;
		}
	}

}
