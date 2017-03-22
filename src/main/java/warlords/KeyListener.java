package warlords;


import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.HashMap;
import java.util.List;

public class KeyListener {

	private Scene scene;
	private List<KeyboardInput> playerList;


	public KeyListener(Scene scene, List<KeyboardInput> playerList) {
		this.scene = scene;
		this.playerList = playerList;
	}

	public void StartListening() {
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				for ( KeyboardInput currentPlayer: playerList) {
					currentPlayer.keyPress(event.getCode());
				}
			}
		});
	}

}