package warlords;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class GameView extends Application {

	static private Scene scene;

	/*GameView() {
		//launch();
		//Application.launch(GameView.class);
	}*/

	@Override
	public void start(Stage primaryStage) {
		GridPane gridPane = new GridPane();
		scene = new Scene(gridPane);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	static public Scene getScene() {
		return scene;
	}
}
