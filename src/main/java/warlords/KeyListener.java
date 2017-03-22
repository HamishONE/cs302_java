package warlords;

import javafx.scene.Scene;
import java.util.List;

public class KeyListener {

	private Scene scene;
	private List<KeyboardInput> playerList;

	public KeyListener(Scene scene, List<KeyboardInput> playerList) {
		this.scene = scene;
		this.playerList = playerList;
	}

	public void startListening() {

		scene.setOnKeyPressed(event -> {
			for (KeyboardInput currentPlayer: playerList) {
				currentPlayer.keyPress(event.getCode());
			}
		});

		scene.setOnKeyReleased(event -> {
			for (KeyboardInput currentPlayer: playerList) {
				currentPlayer.keyRelease(event.getCode());
			}
		});
	}

}
