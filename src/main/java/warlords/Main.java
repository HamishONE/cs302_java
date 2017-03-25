package warlords;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	private static Stage stage;
	private MainController mainController;

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

		mainController = new MainController(600, 800);
		mainController.start();

		AnimationTimer timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				mainController.runLoop();
			}
		};
		timer.start();
	}
}
