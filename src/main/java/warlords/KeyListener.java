package warlords;

import javafx.scene.Scene;
import java.util.List;

/**
 * Class to manage listening to the keyboard for any key events, such as key presses or releases.
 */
public class KeyListener {

	private Scene scene;
	private List<KeyboardInput> playerList;

	/**
	 * Creates new key listener to listen for keypresses
	 *
	 * @param scene			Scene to be listening on
	 * @param playerList	List of actual players (Keyboard inputs)
	 */
	public KeyListener(Scene scene, List<KeyboardInput> playerList) {
		this.scene = scene;
		this.playerList = playerList;
	}

	/**
	 * Starts the listener to register key presses
	 */
	public void startListening() {

		//Handling for keys being pressed
		scene.setOnKeyPressed(event -> {
			//Check which player the key "belongs" to and action on it
			for (KeyboardInput currentPlayer: playerList) {
				currentPlayer.keyPress(event.getCode());
			}
		});

		//Handling for keys being released
		scene.setOnKeyReleased(event -> {
			//Check which player the key "belongs" to and action on it
			for (KeyboardInput currentPlayer: playerList) {
				currentPlayer.keyRelease(event.getCode());
			}
		});
	}

}
