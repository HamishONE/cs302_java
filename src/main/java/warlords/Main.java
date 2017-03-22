package warlords;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	private static Stage stage;

	public static void setScene(Scene scene) {
		stage.setScene(scene);
	}

	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage primaryStage) {

		stage = primaryStage;
		primaryStage.setTitle("CS302 Java Game");
		primaryStage.show();

		new MainController().beginGame();
	}
}
